package logan.blockpartycompose.ui.screens.tutorialMode

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import logan.blockpartycompose.R
import logan.blockpartycompose.ui.components.BaseHeader
import logan.blockpartycompose.ui.components.HelpCard
import logan.blockpartycompose.ui.components.TutorialFailureScreen
import logan.blockpartycompose.ui.components.TutorialSuccessScreen
import logan.blockpartycompose.ui.components.TutorialWindow
import logan.blockpartycompose.ui.screens.level.Direction
import logan.blockpartycompose.ui.screens.level.GameState
import logan.blockpartycompose.ui.screens.level.LevelFooter
import logan.blockpartycompose.ui.screens.level.LevelGrid

@Composable
fun TutorialModeScreen(navController: NavController) {
    val tutorialViewModel: TutorialModeViewModel = hiltViewModel()

    val tutorialState by tutorialViewModel.tutorialState.observeAsState()
    val gamePlayState by tutorialViewModel.state.observeAsState()
    val infoState by tutorialViewModel.isInfoClicked.observeAsState()

    if (tutorialState == null) {
        tutorialViewModel.startTutorial(context = LocalContext.current)
        return
    }

    if (gamePlayState == null) {
        return
    }

    Crossfade(
        targetState = gamePlayState!!.gameState,
        animationSpec = tween(700, delayMillis = 0)
    ) { gameState ->
        if (gameState == GameState.SUCCESS) {
            TutorialSuccessScreen(
                nextLevelOnClick = {
                    if (tutorialState!!.first == 4)
                        navController.popBackStack("welcome", false)
                    tutorialViewModel.nextLevelOnClick { navController.navigate("playMenu") }
                },
                movesUsed = gamePlayState!!.movesUsed,
                tutorialState = tutorialState!!.first
            )
            return@Crossfade
        }
        if (gameState == GameState.FAILED) {
            TutorialFailureScreen(
                tryAgainOnClick = tutorialViewModel::tryAgain
            )
            return@Crossfade
        }

        TutorialMode(
            movesUsed = gamePlayState!!.movesUsed,
            x = tutorialViewModel.level.x,
            blocks = gamePlayState!!.blocks,
            isHelpEnabled = infoState ?: false,
            surroundingBlocks = gamePlayState!!.glowingBlocks,
            tutorialStage = tutorialState!!.first,
            tutorialProgress = tutorialState!!.second,
            forwardOnClick = tutorialViewModel::progressForward,
            blockClicked = tutorialViewModel::blockClicked,
            undoClicked = { tutorialViewModel.undoClicked() },
            restartClicked = { tutorialViewModel.tryAgain() },
            infoClicked = { tutorialViewModel.infoClicked() },
            direction = gamePlayState!!.direction
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TutorialMode(
    movesUsed: Int,
    x: Int,
    blocks: List<Char>,
    surroundingBlocks: List<Int>,
    isHelpEnabled: Boolean = false,
    forwardOnClick: (() -> Unit)? = null,
    tutorialProgress: Int = 0,
    tutorialStage: Int,
    blockClicked: (Char, Int) -> Unit,
    undoClicked: () -> Unit,
    restartClicked: () -> Unit,
    infoClicked: () -> Unit,
    direction: Direction?,
) {
    BoxWithConstraints() {
        var gridSize = (maxWidth.value / (x + 1)).dp
        if (x == 4) gridSize -= 25.dp
        val contentHeight = (gridSize.value * (x + 2)) + 150f
        if (((maxHeight.value * .8f) <= contentHeight)) gridSize /= 1.25f
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxHeight()
        ) {
            TutorialHeader(movesUsed)
            Crossfade(
                targetState = isHelpEnabled, modifier = Modifier
                    .weight(.8f)
                    .fillMaxHeight()
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    LevelGrid(
                        blockClicked = blockClicked,
                        x = x,
                        blocks = blocks,
                        gridSize = gridSize,
                        direction = direction ?: Direction.DOWN,
                        glowList = surroundingBlocks
                    )
                    if (it) HelpCard(count = 6)
                    else TutorialWindow(tutorialStage, tutorialProgress, forwardOnClick)
                }
            }
            if (tutorialStage >= 3)
                LevelFooter(undoClicked, restartClicked, infoClicked)
        }
    }
}


@Composable
fun TutorialHeader(movesUsed: Int) {
    BaseHeader(
        middleContent = {
            Text(
                text = stringResource(id = R.string.moves_used, movesUsed),
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp)
            )
        })
}