package logan.blockpartycompose.ui.screens.level

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import logan.blockpartycompose.ui.components.*
import logan.blockpartycompose.ui.screens.levelsMenu.GameState
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import logan.blockpartycompose.ui.screens.levelsMenu.LevelsViewModel

@ExperimentalFoundationApi
@Composable
fun LevelController(
    navigation: NavController,
    levelSet: LevelSet,
    name: String,
    viewModel: LevelsViewModel = hiltViewModel()
) {
    val state by viewModel.state.observeAsState()

    if (state != null) {
        when (state!!.gameState) {
            GameState.SUCCESS -> {
                val nextLevel = state!!.name.toInt() + 1
                val newLevelSet: LevelSet = if(nextLevel >= 11) LevelSet.MEDIUM
                    else LevelSet.EASY
                SuccessScreen(
                    // not great logic tbh the name/levelSet of the parent composable don't change
                    nextLevelOnClick = { viewModel.setupLevel(newLevelSet, nextLevel.toString()) },
                    backClicked = { navigation.navigateUp() },
                    movesUsed = state!!.movesUsed,
                    levelName = state!!.name
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
                    movesUsed = state!!.movesUsed,
                    x = state!!.x,
                    blocks = state!!.blocks,
                    blockClicked = viewModel::blockClicked,
                    backClicked = { navigation.navigateUp() },
                    solveClicked = viewModel::solveLevel,
                    resetClicked = viewModel::tryAgain
                )
            }
        }
    } else {
        viewModel.setupLevel(levelSet, name)
    }
}

@ExperimentalFoundationApi
@Composable
fun Level(
    movesUsed: Int,
    x: Int,
    blocks: List<Char>,
    blockClicked: (Char, Int) -> Unit,
    backClicked: () -> Unit,
    solveClicked:() -> Unit,
    resetClicked: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxHeight()
    ) {
        LevelHeader(movesUsed, backClicked)
        LevelGrid(blockClicked, x, blocks)
        LevelFooter(solveClicked, resetClicked)
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
fun BackIcon(backClicked: () -> Unit, modifier: Modifier = Modifier) {
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
            val onClick = { blockClicked(blocks[index], index) }

            when (blocks[index]) {
                'r' -> {
                    RedBox(onClick)
                }
                'b' -> {
                    BlueBox(onClick)
                }
                'g' -> {
                    GreenBox(onClick)
                }
                'y' -> {
                    YellowBox(onClick)
                }
                '.' -> {
                    GrayBox(onClick)
                }
                'x' -> {
                    BlackBox(onClick)
                }
            }
        }
    }
}

@Composable
fun LevelFooter(solveClicked: () -> Unit, resetClicked: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = resetClicked,
        ) {
            Icon(Icons.Filled.Clear, contentDescription = "Clear")
        }
        IconButton(
            onClick = solveClicked,
        ) {
            Icon(Icons.Filled.PlayArrow, contentDescription = "Hint")
        }
        IconButton(
            onClick = { /*TODO*/ },
        ) {
            Icon(Icons.Filled.Info, contentDescription = "Undo")
        }
    }
}

@Composable
fun SuccessScreen(
    nextLevelOnClick: () -> Unit,
    backClicked: () -> Unit,
    movesUsed: Int,
    levelName: String
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
fun FailureScreen(tryAgainOnClick: () -> Unit, backClicked: () -> Unit) {
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
                Text(text = "You Died!")
                Button(onClick = { tryAgainOnClick() }) {
                    Text(text = "Try Again")
                }
            }
        }
    }
}