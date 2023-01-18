package logan.blockpartycompose.ui.screens.tutorialMode

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import logan.blockpartycompose.ui.screens.level.LevelHeader

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
        TutorialHeader(movesUsed)
        val padding = if (tutorialStage >= 3) 20.dp else 60.dp
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(top = padding)
                .fillMaxHeight(.85f)
        ) {
            TutorialSectionOne(
                blockClicked = blockClicked,
                x = x,
                blocks = blocks,
                surroundingBlocks = surroundingBlocks,
                direction = direction
            )
            if (tutorialStage >= 3) {
                Crossfade(
                    targetState = isHelpEnabled,
                    animationSpec = tween(300)
                ) { isHelpEnabled ->
                    if (isHelpEnabled) {
                        HelpCard(6)
                    } else
                        TutorialWindow(tutorialStage, tutorialProgress, forwardOnClick)
                }
            } else {
                TutorialWindow(tutorialStage, tutorialProgress, forwardOnClick)
            }
        }
        if (tutorialStage >= 3)
            LevelFooter(undoClicked, restartClicked, infoClicked)
        else
            Spacer(modifier = Modifier.height(50.dp))
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
