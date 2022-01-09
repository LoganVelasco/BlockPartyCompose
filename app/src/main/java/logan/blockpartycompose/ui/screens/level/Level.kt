package logan.blockpartycompose.ui.screens.level

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import logan.blockpartycompose.R
import logan.blockpartycompose.ui.components.*
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet


@ExperimentalFoundationApi
@Composable
fun Level(
    navigateUp: () -> Unit,
    levelSet: LevelSet,
    name: String,
    viewModel: LevelsViewModel = hiltViewModel()
) {
    val state by viewModel.state.observeAsState()

    if (state == null) {
        viewModel.setupLevel(levelSet, name)
    } else {
        when (state!!) {
            is InProgress -> {
                (state as InProgress).apply {
                    LevelScreen(
                        movesUsed = movesUsed,
                        x = x,
                        blocks = blocks,
                        navigateUp = navigateUp,
                        onClicks = viewModel.onClicks
                    )
                }
            }
            is Success -> {
                (state as Success).apply {
                    SuccessScreen(
                        nextLevelOnClick = { viewModel.nextLevel() },
                        backClicked = navigateUp,
                        movesUsed = movesUsed,
                        levelName = name,
                        isNewHighScore = viewModel.isHighScoreUpdated()
                    )
                }
            }
            is Failure -> {
                FailureScreen(
                    tryAgainOnClick = viewModel::tryAgain,
                    backClicked = navigateUp
                )
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun LevelScreen(
    movesUsed: Int,
    x: Int,
    blocks: List<Char>,
    navigateUp: () -> Unit,
    onClicks: LevelOnClicks
) {
    onClicks.apply {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxHeight()
        ) {
            LevelHeader(movesUsed, navigateUp)
            LevelGrid(blockClicked, x, blocks)
            LevelFooter(solveClicked, resetClicked)
        }
    }
}

@Composable
fun LevelHeader(movesUsed: Int, navigateUp: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 10.dp)
    ) {
        BackIcon(navigateUp)
        Text(
            text = "Moves Used:  $movesUsed",
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun BackIcon(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(
        onClick = onClick,
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
    levelName: String,
    isNewHighScore: Boolean
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
                if (isNewHighScore) {
                    Text(text = "New High Score!")
                    Text(text = "Level $levelName Completed in $movesUsed moves!")
                } else {
                    Text(text = "You Did it!")
                    Text(text = "Level $levelName Completed in $movesUsed moves!")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_undo),
                        contentDescription = "Star"
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_undo),
                        contentDescription = "Star"
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_undo),
                        contentDescription = "Star"
                    )
                }
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