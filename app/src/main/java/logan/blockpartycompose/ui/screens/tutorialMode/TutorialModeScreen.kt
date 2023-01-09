package logan.blockpartycompose.ui.screens.tutorialMode

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import logan.blockpartycompose.ui.components.TutorialFailureScreen
import logan.blockpartycompose.ui.components.TutorialSuccessScreen
import logan.blockpartycompose.ui.components.TutorialWindow
import logan.blockpartycompose.ui.screens.level.Direction
import logan.blockpartycompose.ui.screens.level.GameState
import logan.blockpartycompose.ui.screens.level.LevelFooter
import logan.blockpartycompose.ui.screens.level.LevelGrid
import logan.blockpartycompose.ui.screens.level.LevelHeader

@Composable
fun TutorialModeScreen(navController: NavController) {
    val tutorialViewModel: TutorialModeViewModel = hiltViewModel()

    val tutorialState by tutorialViewModel.tutorialState.observeAsState()
    val gamePlayState by tutorialViewModel.state.observeAsState()

    if (tutorialState == null) {
        tutorialViewModel.startTutorial(context = LocalContext.current)
        return
    }

    if (gamePlayState == null) {
        return
    }

    Crossfade(
        targetState = gamePlayState!!.gameState,
        animationSpec = tween(750, delayMillis = 100)
    ) { gameState ->

        if (gameState == GameState.SUCCESS) {
            TutorialSuccessScreen(
                nextLevelOnClick = {
                    if(tutorialState!!.first == 4)
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
            surroundingBlocks = gamePlayState!!.glowingBlocks,
            tutorialStage = tutorialState!!.first,
            tutorialProgress = tutorialState!!.second,
            backOnClick = tutorialViewModel::progressBackward,
            forwardOnClick = tutorialViewModel::progressForward,
            blockClicked = tutorialViewModel::blockClicked,
            backClicked = { navController.navigateUp() },
            settingsClicked = { navController.navigateUp() },
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
    backOnClick: (() -> Unit)? = null,
    forwardOnClick: (() -> Unit)? = null,
    tutorialProgress: Int = 0,
    tutorialStage: Int,
    blockClicked: (Char, Int) -> Unit,
    backClicked: () -> Unit,
    settingsClicked: () -> Unit,
    undoClicked: () -> Unit,
    restartClicked: () -> Unit,
    infoClicked: () -> Unit,
    direction: Direction?,
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxHeight()
    ) {
        LevelHeader(movesUsed, backClicked, settingsClicked)
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.padding(top = 75.dp).fillMaxHeight()
        ) {
            TutorialSectionOne(
                blockClicked = blockClicked,
                x = x,
                blocks = blocks,
                surroundingBlocks = surroundingBlocks,
                direction = direction
            )
            Spacer(modifier = Modifier.height(20.dp))
//            Crossfade(targetState = isHelpEnabled, animationSpec = tween(300)) { isHelpEnabled ->
//                if (isHelpEnabled) {
//                    HelpCard(infoProgress)
//                } else Spacer(modifier = Modifier.height(200.dp))
//            }
            TutorialWindow(tutorialStage, tutorialProgress, backOnClick, forwardOnClick)
        }
        if(tutorialStage >= 3)
            LevelFooter(undoClicked, restartClicked, infoClicked)
    }
}


@Composable
fun TutorialHeader() {

}

@Composable
fun TutorialFooter() {

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TutorialSectionOne(
    blockClicked: (Char, Int) -> Unit,
    x: Int,
    blocks: List<Char>,
    surroundingBlocks: List<Int>,
    direction: Direction?
) {
    LevelGrid(blockClicked, x, blocks, direction ?: Direction.DOWN, surroundingBlocks)

//    "Welcome to Block Party, The object of the game is to move the blue square to the gold square"
//    "Tap a surrounding block to move"
//    ""
}
