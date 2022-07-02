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
import logan.blockpartycompose.ui.components.*
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import logan.blockpartycompose.ui.screens.playMenu.EmptyStar
import logan.blockpartycompose.ui.screens.playMenu.FilledStar

@ExperimentalFoundationApi
@Composable
fun LevelScreen(
    navigation: NavController,
    levelSet: LevelSet,
    name: Int,
    viewModel: LevelsViewModel = hiltViewModel()
) {
    val state by viewModel.state.observeAsState()

    if (state != null) {
        when (state!!.gameState) {
            GameState.SUCCESS -> {
                val nextLevel = state!!.name + 1
                val stars = if (state!!.movesUsed <= viewModel.getMinMoves()) 3 else if(state!!.movesUsed-2 <= viewModel.getMinMoves()) 2 else 1
                viewModel.updateLevel(levelSet, state!!.name, stars)
                SuccessScreen(
                    // not great logic tbh the name/levelSet of the parent composable don't change
                    nextLevelOnClick = {
                                            viewModel.setupLevel(levelSet, nextLevel)
                                       },
                    backClicked = { navigation.navigateUp() },
                    movesUsed = state!!.movesUsed,
                    stars = stars,
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
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxHeight()
    ) {
        LevelHeader(movesUsed, backClicked)
        LevelGrid(blockClicked, x, blocks)
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
fun SuccessScreen(
    nextLevelOnClick: () -> Unit,
    backClicked: () -> Unit,
    movesUsed: Int,
    levelName: Int,
    stars: Int
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
                SuccessStars(stars)
                Button(onClick = { nextLevelOnClick() }) {
                    Text(text = "Next Level")
                }
            }
        }
    }
}

@Composable
private fun SuccessStars(result: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        if(result >= 1){
            FilledStar()
        }else EmptyStar()
        if(result >= 2) {
            FilledStar()
        }else EmptyStar()
        if(result >= 3){
            FilledStar()
        }else EmptyStar()
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