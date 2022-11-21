package logan.blockpartycompose.ui.screens.customLevels

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import logan.blockpartycompose.R
import logan.blockpartycompose.ui.screens.level.BackIcon
import logan.blockpartycompose.ui.screens.level.GameState
import logan.blockpartycompose.ui.screens.level.LevelScreen
import logan.blockpartycompose.ui.screens.level.LevelViewModel
import logan.blockpartycompose.ui.screens.levelBuilder.LevelBuilderViewModel
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet

@ExperimentalFoundationApi
@Composable
fun CustomLevelScreen(
    navigation: NavController,
    id: Int,
    levelsViewModel: LevelViewModel = hiltViewModel()
) {
    val backEntry = remember { navigation.previousBackStackEntry!! }
    val levelBuilderViewModel: LevelBuilderViewModel = hiltViewModel(backEntry)

    val state by levelsViewModel.state.observeAsState()

    if (state != null) {
        when (state!!.gameState) {
            GameState.SUCCESS -> {
                CustomLevelEnd(
                    restartClicked = {
                        levelsViewModel.tryAgain()
                        navigation.popBackStack("level/{levelSet}/{name}", inclusive = false)
                    },
                    editClicked = {
                        levelsViewModel.tryAgain()
                        levelBuilderViewModel.setupExistingLevel(levelsViewModel.level)
                        if (id != -1) navigation.navigate("levelBuilder/$id")
                        else navigation.popBackStack("levelBuilder", inclusive = false)
                    },
                    backClicked = {
                        navigation.navigateUp()
                    },
                    message = stringResource(R.string.you_did_it)
                )
            }
            GameState.FAILED -> {
                CustomLevelEnd(
                    restartClicked = { levelsViewModel.tryAgain() },
                    editClicked = {
                        levelsViewModel.tryAgain()
                        levelBuilderViewModel.setupExistingLevel(levelsViewModel.level)
                        navigation.navigate("levelBuilder")
                    },
                    backClicked = { navigation.navigateUp() },
                    message = stringResource(R.string.level_failed)
                )
            }
            GameState.IN_PROGRESS -> {
                LevelScreen(
                    movesUsed = state!!.movesUsed,
                    x = levelsViewModel.level.x,
                    blocks = state!!.blocks,
                    blockClicked = levelsViewModel::blockClicked,
                    backClicked = { navigation.navigateUp() },
                    settingsClicked = { navigation.navigateUp() },
                    undoClicked = { levelsViewModel.undoClicked() },
                    restartClicked = { levelsViewModel.tryAgain() },
                    infoClicked = {},
                    direction = state!!.direction
                )
            }
        }
    } else {
        if (id != -1) levelsViewModel.setupLevel(LevelSet.CUSTOM, id)
        else
            levelsViewModel.setupLevel(levelBuilderViewModel.level)
    }
}


@Composable
fun CustomLevelEnd(
    restartClicked: () -> Unit,
    editClicked: () -> Unit,
    backClicked: () -> Unit,
    message: String
) {
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Column {
            BackIcon(backClicked, Modifier.padding(10.dp))
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Text(text = message)
                Button(onClick = { restartClicked() }) {
                    Text(text = stringResource(id = R.string.try_again))
                }
                Button(onClick = { editClicked() }) {
                    Text(text = stringResource(R.string.edit_level))
                }
            }
        }
    }
}