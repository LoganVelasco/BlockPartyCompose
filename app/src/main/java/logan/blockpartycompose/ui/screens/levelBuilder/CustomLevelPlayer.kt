package logan.blockpartycompose.ui.screens.levelBuilder

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
import androidx.lifecycle.ViewModelStoreOwner
import logan.blockpartycompose.ui.screens.level.*

@ExperimentalFoundationApi
@Composable
fun CustomLevelPlayer(
    onNavigate: (String) -> Unit,
    navigateUp: () -> Unit,
    navigationId: ViewModelStoreOwner,
    levelBuilderViewModel: LevelBuilderViewModel = hiltViewModel(navigationId),
    levelsViewModel: LevelsViewModel = hiltViewModel()
) {
    val state by levelsViewModel.state.observeAsState()

    if (state == null) {
        levelsViewModel.setupLevel(levelBuilderViewModel.level)
    } else {
        when (state!!) {
            is InProgress -> {
                (state as InProgress).apply {
                    LevelScreen(
                        movesUsed = movesUsed,
                        x = x,
                        blocks = blocks,
                        navigateUp = navigateUp,
                        onClicks = levelsViewModel.onClicks,
                    )
                }
            }
            is Success -> {
                CustomLevelEndScreen(
                    restartClicked = { levelsViewModel.tryAgain() },
                    editClicked = { onNavigate("levelBuilder") },
                    backClicked = navigateUp,
                    message = "You Did It!"
                )
            }
            is Failure -> {
                CustomLevelEndScreen(
                    restartClicked = { levelsViewModel.tryAgain() },
                    editClicked = { onNavigate("levelBuilder") },
                    backClicked = navigateUp,
                    message = "Level Failed"
                )
            }
        }
    }
}


@Composable
fun CustomLevelEndScreen(
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
        Column() {
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