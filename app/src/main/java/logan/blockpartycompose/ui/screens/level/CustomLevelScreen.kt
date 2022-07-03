package logan.blockpartycompose.ui.screens.level

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import logan.blockpartycompose.ui.screens.levelBuilder.LevelBuilderViewModel

@ExperimentalFoundationApi
@Composable
fun CustomLevelScreen(
    navigation: NavController,
    levelBuilderViewModel: LevelBuilderViewModel = hiltViewModel(
        navigation.getViewModelStoreOwner(
            navigation.graph.id
        )
    ),
    levelsViewModel: LevelsViewModel = hiltViewModel()
) {

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
                        navigation.popBackStack("levelBuilder", inclusive = false)
                    },
                    backClicked = {
                        navigation.popBackStack("levelBuilder", inclusive = false)
                    },
                    message = "You Did It!"
                )
            }
            GameState.FAILED -> {
                CustomLevelEnd(
                    restartClicked = { levelsViewModel.tryAgain() },
                    editClicked = { navigation.navigate("levelBuilder") },
                    backClicked = { navigation.navigateUp() },
                    message = "Level Failed"
                )
            }
            GameState.IN_PROGRESS -> {
                LevelScreen(
                    movesUsed = state!!.movesUsed,
                    x = state!!.x,
                    blocks = state!!.blocks,
                    blockClicked = levelsViewModel::blockClicked,
                    backClicked = { navigation.navigateUp() }
                )
            }
        }
    } else {
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
                    Text(text = "Restart")
                }
                Button(onClick = { editClicked() }) {
                    Text(text = "Edit Level")
                }
            }
        }
    }
}