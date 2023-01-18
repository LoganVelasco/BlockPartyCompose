package logan.blockpartycompose.ui.screens.level

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import logan.blockpartycompose.R
import logan.blockpartycompose.ui.components.*
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import logan.blockpartycompose.utils.GameUtils.Companion.levelGridTransitions

@ExperimentalFoundationApi
@Composable
fun LevelController(
    navigation: NavController,
    levelSet: LevelSet,
    name: Int,
    viewModel: LevelViewModel = hiltViewModel()
) {
    val state by viewModel.state.observeAsState()

    val infoState by viewModel.infoState.observeAsState()

    if (state == null) {
        viewModel.setupLevel(levelSet, name)
        return
    }

    Crossfade(
        targetState = state!!.gameState,
        animationSpec = tween(750, delayMillis = 100)
    ) { gameState ->
        when (gameState) {
            GameState.SUCCESS -> {
                if (state!!.movesUsed == 0) return@Crossfade // TODO figure out why recomposing here instead of catching

                val nextLevel = viewModel.level.id + 1
                val isFinalLevel = viewModel.isFinalLevel()
                val stars = viewModel.getStars(state!!.movesUsed)

                val nextLevelOnClick = if (isFinalLevel) {
                    { navigation.navigate("playMenu") }
                } else {
                    { viewModel.setupLevel(levelSet, nextLevel) }
                }

                SuccessScreen(
                    // not great logic tbh the name/levelSet of the parent composable don't change
                    nextLevelOnClick = nextLevelOnClick,
                    tryAgainOnClick = viewModel::tryAgain,
                    backClicked = { navigation.navigateUp() },
                    movesUsed = state!!.movesUsed,
                    stars = stars,
                    levelName = viewModel.level.name,
                    minMoves = viewModel.level.minMoves,
                    isFinalLevel = isFinalLevel
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
                    infoState = infoState?: 0,
                    infoProgress = viewModel.getInfoProgress(),
                    blockClicked = viewModel::blockClicked,
                    backClicked = { navigation.navigateUp() },
                    settingsClicked = { navigation.navigateUp() },
                    undoClicked = { viewModel.undoClicked() },
                    restartClicked = { viewModel.tryAgain() },
                    infoClicked = { viewModel.infoClicked() },
                    direction = state!!.direction
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
    infoState: Int = -1,
    infoProgress: Int = 0,
    blockClicked: (Char, Int) -> Unit,
    backClicked: () -> Unit,
    settingsClicked: () -> Unit,
    undoClicked: () -> Unit,
    restartClicked: () -> Unit,
    infoClicked: () -> Unit,
    direction: Direction?
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxHeight()
    ) {
        LevelHeader(movesUsed, backClicked, settingsClicked)
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(top = 75.dp)
        ) {
            LevelGrid(blockClicked, x, blocks, direction ?: Direction.DOWN)
            Spacer(modifier = Modifier.height(20.dp))
            Crossfade(targetState = infoState != -1, animationSpec = tween(300)) { isInfoEnabled ->
                if (isInfoEnabled) {
                    HelpCard(count = infoProgress, currentCard = infoState)
                } else Spacer(modifier = Modifier.height(175.dp))
            }
        }
        LevelFooter(undoClicked, restartClicked, infoClicked)
    }
}

@Composable
fun LevelHeader(movesUsed: Int, backClicked: () -> Unit, settingsClicked: () -> Unit) {
    BaseHeader(
        startIcon = Icons.Filled.ArrowBack,
        firstIconOnclick = backClicked,
//        endIcon = Icons.Filled.Settings,
        endIconOnclick = settingsClicked,
        middleContent = {
            Text(
                text = stringResource(id = R.string.moves_used, movesUsed),
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp)
            )
        })
}


@OptIn(ExperimentalAnimationApi::class)
@ExperimentalFoundationApi
@Composable
fun LevelGrid(
    blockClicked: (Char, Int) -> Unit,
    x: Int,
    blocks: List<Char>,
    direction: Direction = Direction.DOWN,
    glowList: List<Int> = emptyList()
) {
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(x),
            contentPadding = PaddingValues(5.dp),
            modifier = Modifier
                .padding(15.dp)
                .border(2.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .fillMaxWidth()
                .testTag(stringResource(id = R.string.level))
        ) {
            items(blocks.size) { index ->
                val onClick = { blockClicked(blocks[index], index) }
                AnimatedContent(
                    targetState = blocks[index],
                    transitionSpec = {
                        levelGridTransitions(
                            this.initialState,
                            this.targetState,
                            direction
                        )
                    }
                ) { type ->
                    when (type) {
                        'e' -> {
                            EnemyBlock(onClick = onClick, isPulsing = glowList.contains(index))
                        }

                        'p' -> {
                            PlayerBlock(onClick = onClick, isPulsing = glowList.contains(index))
                        }

                        'm' -> {
                            MovableBlock(onClick = onClick, isPulsing = glowList.contains(index))
                        }

                        'g' -> {
                            GoalBlock(onClick = onClick, isPulsing = glowList.contains(index))
                        }

                        '.' -> {
                            EmptyBlock(onClick = onClick, isPulsing = glowList.contains(index))
                        }

                        'x' -> {
                            UnmovableBlock(onClick = onClick, isPulsing = glowList.contains(index))
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun LevelFooter(
    undoClicked: () -> Unit,
    restartClicked: () -> Unit,
    infoClicked: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = undoClicked,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_undo_24),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = "Undo",
            )
        }
        IconButton(
            onClick = restartClicked,
        ) {
            Icon(Icons.Filled.Refresh, tint = MaterialTheme.colorScheme.onSurfaceVariant, contentDescription = "Restart")
        }
        IconButton(
            onClick = infoClicked,
        ) {
            Icon(Icons.Filled.Info, tint = MaterialTheme.colorScheme.onSurfaceVariant, contentDescription = "Info")
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
    minMoves: Int,
    isFinalLevel: Boolean
) {
    if (movesUsed == 0) return
    PostLevelScreen(backClicked = backClicked) {
        Text(text = stringResource(id = R.string.you_did_it), fontSize = 36.sp)
        Text(text = stringResource(id = R.string.level_completed_in, levelName, movesUsed), fontSize = 22.sp)
        if (stars < 3) Text(text = stringResource(id = R.string.complete_in, minMoves))
        SuccessStars(stars)
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { tryAgainOnClick() }) {
                Text(text = stringResource(R.string.try_again))
            }
            Button(onClick = { nextLevelOnClick() }) {
                Text(text = stringResource(R.string.next_level))
            }
        }
    }
}



@Composable
fun FailureScreen(tryAgainOnClick: () -> Unit, backClicked: () -> Unit) {
    PostLevelScreen(backClicked = backClicked) {
        Text(text = stringResource(R.string.you_died))
        Button(onClick = { tryAgainOnClick() }) {
            Text(text = stringResource(id = R.string.try_again))
        }
    }
}

@Composable
fun PostLevelScreen(
    backClicked: (() -> Unit)? = null,
    content: @Composable() () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Column {
            if(backClicked != null){  BackIcon(backClicked, Modifier.padding(10.dp)) }
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                content()
            }
        }
    }
}

@Composable
fun SuccessStars(result: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .testTag(stringResource(R.string.stars))
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