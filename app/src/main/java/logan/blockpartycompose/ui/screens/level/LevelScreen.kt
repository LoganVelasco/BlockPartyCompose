package logan.blockpartycompose.ui.screens.level

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import logan.blockpartycompose.ui.components.*
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet

@ExperimentalFoundationApi
@Composable
fun LevelController(
    navigation: NavController,
    levelSet: LevelSet,
    name: Int,
    viewModel: LevelsViewModel = hiltViewModel()
) {
    val state by viewModel.state.observeAsState()

    if (state != null) {
        when (state!!.gameState) {
            GameState.SUCCESS -> {
                val nextLevel = viewModel.level.id + 1
                val isFinalLevel = (nextLevel == 11 || nextLevel == 21 || nextLevel == 31)
                val stars = viewModel.getStars(state!!.movesUsed)
                viewModel.updateLevel(levelSet, viewModel.level.id, stars)
                SuccessScreen(
                    // not great logic tbh the name/levelSet of the parent composable don't change
                    nextLevelOnClick = {
                        viewModel.setupLevel(levelSet, nextLevel)
                    },
                    tryAgainOnClick = viewModel::tryAgain,
                    backClicked = { navigation.navigateUp() },
                    movesUsed = state!!.movesUsed,
                    stars = stars,
                    levelName = viewModel.level.name,
                    minMoves = viewModel.level.minMoves
                )
            }
            GameState.FAILED -> {
                FailureScreen(
                    tryAgainOnClick = viewModel::tryAgain,
                    backClicked = { navigation.navigateUp() }
                )
            }
            GameState.IN_PROGRESS -> {
                LevelScreen(
                    movesUsed = state!!.movesUsed,
                    x = viewModel.level.x,
                    blocks = state!!.blocks,
                    blockClicked = viewModel::blockClicked,
                    backClicked = { navigation.navigateUp() },
                    settingsClicked = { navigation.navigateUp() }
                )
            }
        }
    } else {
        viewModel.setupLevel(levelSet, name)
    }
}

@ExperimentalFoundationApi
@Composable
fun LevelScreen(
    movesUsed: Int,
    x: Int,
    blocks: List<Char>,
    blockClicked: (Char, Int) -> Unit,
    backClicked: () -> Unit,
    settingsClicked: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxHeight()
    ) {
        LevelHeader(movesUsed, backClicked, settingsClicked)
        LevelGrid(blockClicked, x, blocks)
        LevelFooter()
    }
}

@Composable
fun LevelHeader(movesUsed: Int, backClicked: () -> Unit, settingsClicked: () -> Unit) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(end = 10.dp)
//    ) {
//        BackIcon(backClicked)
//        Text(
//            text = "Moves Used:  $movesUsed",
//            textAlign = TextAlign.Center,
//        )
//    }
    BaseHeader(
        firstIcon = Icons.Filled.ArrowBack,
        firstIconOnclick = backClicked,
        endIcon = Icons.Filled.Settings,
        endIconOnclick = settingsClicked,
        middleContent = {
            Text(
                text = "Moves Used:  $movesUsed",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp)
            )
        })
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
            Icon(Icons.Filled.Refresh, contentDescription = "Undo")
        }
        IconButton(
            onClick = { /*TODO*/ },
        ) {
            Icon(Icons.Filled.Info, contentDescription = "Info")
        }
        IconButton(
            onClick = { /*TODO*/ },
        ) {
            Icon(Icons.Filled.Star, contentDescription = "Hint")
        }
    }
}

@Composable
fun SuccessScreen(
    nextLevelOnClick: () -> Unit,
    tryAgainOnClick: () -> Unit,
    backClicked: () -> Unit,
    movesUsed: Int,
    levelName: String,
    stars: Int,
    minMoves: Int
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
                Text(text = "$levelName Completed in $movesUsed moves!")
                if (stars < 3) Text(text = "Complete in $minMoves moves for 3 stars")
                SuccessStars(stars)
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = { tryAgainOnClick() }) {
                        Text(text = "Try Again")
                    }
                    Button(onClick = { nextLevelOnClick() }) {
                        Text(text = "Next Level")
                    }
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
        if (result >= 1) {
            FilledStar()
        } else EmptyStar()
        if (result >= 2) {
            FilledStar()
        } else EmptyStar()
        if (result >= 3) {
            FilledStar()
        } else EmptyStar()
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