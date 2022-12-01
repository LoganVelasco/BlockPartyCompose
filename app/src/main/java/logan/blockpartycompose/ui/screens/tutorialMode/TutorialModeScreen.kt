package logan.blockpartycompose.ui.screens.tutorialMode

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import logan.blockpartycompose.R
import logan.blockpartycompose.ui.screens.level.LevelGrid
import logan.blockpartycompose.ui.screens.level.LevelHeader
import logan.blockpartycompose.ui.screens.level.LevelViewModel
import logan.blockpartycompose.ui.screens.playMenu.PlayMenuViewModel

@Composable
fun TutorialModeScreen(navController: NavController) {
    val tutorialViewModel:TutorialModeViewModel = hiltViewModel()
    val levelViewModel:LevelViewModel = hiltViewModel()

    val tutorialState by tutorialViewModel.state.observeAsState()
    val levelState by levelViewModel.state.observeAsState()



    if(tutorialState == null){
        tutorialViewModel.getTutorialProgress()
        return
    }

    if(levelState == null){
        levelViewModel.setupLevel(tutorialViewModel.getLevel())
        return
    }

    TutorialMode(blockClicked = levelViewModel::blockClicked, levelState!!.blocks)

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TutorialMode(blockClicked: (Char, Int) -> Unit, blocks: List<Char>){
    TutorialHeader()
    LevelGrid(blockClicked = blockClicked, x = 4, blocks = blocks)
    TutorialFooter()
}

@Composable
fun TutorialHeader(){

}

@Composable
fun TutorialFooter(){

}