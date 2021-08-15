package logan.blockpartycompose.ui.screens.level

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.ui.components.*
import logan.blockpartycompose.ui.screens.levelsMenu.GameState
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import logan.blockpartycompose.ui.screens.levelsMenu.LevelState
import logan.blockpartycompose.ui.screens.levelsMenu.LevelsViewModel

@ExperimentalFoundationApi
@Composable
fun LevelScreen(
    navigation: NavController,
    levelSet: LevelSet,
    name: String,
    viewModel: LevelsViewModel = hiltViewModel()
) {
    val state by viewModel.state.observeAsState()

    if(state != null) {
        when(state!!.gameState){
            GameState.SUCCESS -> {
                val nextLevel = state!!.level.name.toInt()+1
                SuccessScreen(
                    // not great logic tbh the name/levelSet of the parent composable don't change
                    nextLevelOnClick = { viewModel.setupLevel(levelSet, nextLevel.toString()) },
                    backClicked = { navigation.navigateUp() },
                    movesUsed = state!!.movesUsed,
                    levelName = state!!.level.name
                )
            }
            GameState.FAILED -> {
                FailureScreen(
                    tryAgainOnClick = viewModel::tryAgain,
                    backClicked = { navigation.navigateUp() }
                )
            }
            GameState.IN_PROGRESS -> {
                Level(
                    state = state!!,
                    blockClicked = viewModel::blockClicked,
                    backClicked = { navigation.navigateUp() }
                )
            }
        }
    }else{
        viewModel.setupLevel(levelSet, name)
    }
}

@ExperimentalFoundationApi
@Composable
fun Level(
    state: LevelState,
    blockClicked: (Char, Int) -> Unit,
    backClicked: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxHeight()
    ) {
        LevelHeader(state.movesUsed, backClicked)
        LevelGrid(blockClicked, state.level.x, state.blocks)
        LevelFooter()
    }
}


@Composable
fun LevelHeader(movesUsed: Int, backClicked: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 10.dp)
    ) {
        BackIcon(backClicked)
        Text(
            text = "Moves Used:  $movesUsed",
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun BackIcon(backClicked: () -> Unit, modifier: Modifier = Modifier){
    IconButton(
        onClick = backClicked,
        modifier = modifier
    ) {
        Icon(Icons.Filled.ArrowBack, contentDescription = "Back to Menu")
    }
}

@ExperimentalFoundationApi
@Composable
fun LevelGrid(blockClicked: (Char, Int) -> Unit, x: Int, blocks: List<Char>) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(x),
        contentPadding = PaddingValues(5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(blocks.size) { index ->
            when (blocks[index]) {
                'r' -> {
                    RedBox{ blockClicked('r', index) }
                }
                'b' -> {
                    BlueBox { blockClicked('b', index) }
                }
                'g' -> {
                    GreenBox{ blockClicked('g', index) }
                }
                'y' -> {
                    YellowBox{ blockClicked('y', index) }
                }
                '.' -> {
                    GrayBox{ blockClicked('.', index) }
                }
                'x' -> {
                    BlackBox { blockClicked('x', index) }
                }
            }
        }
    }
}

@Composable
fun LevelFooter() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = { /*TODO*/ },
        ) {
            Icon(Icons.Filled.Info, contentDescription = "Help")
        }
        IconButton(
            onClick = { /*TODO*/ },
        ) {
            Icon(Icons.Filled.Info, contentDescription = "Hint")
        }
        IconButton(
            onClick = { /*TODO*/ },
        ) {
            Icon(Icons.Filled.Info, contentDescription = "Undo")
        }
    }
}

@Composable
fun SuccessScreen(nextLevelOnClick: () -> Unit, backClicked: () -> Unit, movesUsed: Int, levelName: String) {
    Card(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth()) {
        Column() {
            BackIcon (backClicked, Modifier.padding(10.dp))
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().fillMaxHeight()
            ) {
                Text(text = "You Did it!")
                Text(text = "Level $levelName Completed in $movesUsed moves!")
                Button(onClick = { nextLevelOnClick() }) {
                    Text(text = "Next Level")
                }
            }
        }
    }
}

@Composable
fun FailureScreen(tryAgainOnClick: () -> Unit, backClicked: () -> Unit){
    Card(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth()) {
        Column() {
            BackIcon (backClicked, Modifier.padding(10.dp))
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().fillMaxHeight()
            ) {
                Text(text = "You Died!")
                Button(onClick = { tryAgainOnClick() }) {
                    Text(text = "Try Again")
                }
            }
        }
    }
}