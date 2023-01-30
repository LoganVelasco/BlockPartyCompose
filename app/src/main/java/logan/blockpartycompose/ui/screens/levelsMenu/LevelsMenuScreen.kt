package logan.blockpartycompose.ui.screens.levelsMenu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import logan.blockpartycompose.R
import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.ui.components.BaseHeader
import logan.blockpartycompose.ui.components.EmptyBlock
import logan.blockpartycompose.ui.components.EmptyStar
import logan.blockpartycompose.ui.components.EnemyBlock
import logan.blockpartycompose.ui.components.FilledStar
import logan.blockpartycompose.ui.components.GoalBlock
import logan.blockpartycompose.ui.components.MovableBlock
import logan.blockpartycompose.ui.components.PlayerBlock
import logan.blockpartycompose.ui.components.UnmovableBlock
import logan.blockpartycompose.utils.GameUtils.Companion.EMPTY_BLOCK
import logan.blockpartycompose.utils.GameUtils.Companion.ENEMY_BLOCK
import logan.blockpartycompose.utils.GameUtils.Companion.GOAL_BLOCK
import logan.blockpartycompose.utils.GameUtils.Companion.MOVABLE_BLOCK
import logan.blockpartycompose.utils.GameUtils.Companion.PLAYER_BLOCK
import logan.blockpartycompose.utils.GameUtils.Companion.UNMOVABLE_BLOCK
import java.util.Locale
import kotlin.reflect.KFunction2

@Composable
fun LevelsMenuScreen(
    navController: NavController,
    levelSet: LevelSet,
) {
    val viewModel: LevelsMenuViewModel = hiltViewModel()
    val progress = viewModel.getProgress(levelSet)

    val state by viewModel.state.observeAsState()
    val context = LocalContext.current
    val setup = { viewModel.setupState(levelSet, context) }

    if (state == null) {
        setup()
        return
    }

    if (state!!.levels.isEmpty()) {
        CustomLevelEmpty(navController, levelSet, progress, createNewLevel = {
            navController.navigate("levelBuilder")
        })
        return
    }

    LevelsMenu(
        navController,
        levelSet,
        state!!.levels,
        progress,
        deleteLevel = viewModel::deleteCustomLevelTriggered,
        editLevel = { id -> navController.navigate("levelBuilder/$id") }
    )

    if (state!!.deleteId != null && state!!.deleteName != null) {
        DeletionConfirmationPopup(
            state!!.deleteName!!,
            delete = {
                viewModel.deleteCustomLevel(state!!.deleteId!!, context)
            },
            cancel = setup
        )
    }

}

@Composable
fun DeletionConfirmationPopup(name: String, delete: () -> Unit, cancel: () -> Unit) {
    AlertDialog(
        onDismissRequest = { },
        title = {
            Text(stringResource(R.string.delete_level, name))
        },
        confirmButton =
        {
            Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)) {
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = cancel
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                    Button(
                        onClick = delete
                    ) {
                        Text(stringResource(R.string.delete))
                    }
                }
            }
        }
    )
}

@Composable
fun CustomLevelEmpty(
    navController: NavController,
    levelSet: LevelSet,
    progress: List<Int>,
    createNewLevel: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        LevelTopBar(navController, levelSet, progress)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.no_custom_levels),
                fontSize = 26.sp,
                modifier = Modifier.padding(10.dp)
            )
            Button(onClick = createNewLevel) {
                Text(text = stringResource(R.string.create_custom_level))
            }
        }
    }
}

@Composable
fun LevelsMenu(
    navController: NavController,
    levelSet: LevelSet,
    levels: List<Level>,
    progress: List<Int>,
    editLevel: (Int?) -> Unit = {},
    deleteLevel: KFunction2<Int, String, Unit>? = null
) {
    Column(Modifier.fillMaxSize()) {
        LevelTopBar(navController, levelSet, progress)
        LevelsList(navController, levelSet, levels, progress, editLevel, deleteLevel)
    }
}

@Composable
fun LevelTopBar(
    navController: NavController,
    levelSet: LevelSet,
    progress: List<Int>
) {
    BaseHeader(
        startIcon = Icons.Filled.ArrowBack,
        startIconOnclick = { navController.navigateUp() },
        middleContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(10.dp)
            ) {
                val count = if (progress.isEmpty()) 0 else progress.sum()
                val text =
                    if (levelSet == LevelSet.CUSTOM) stringResource(R.string.my_levels) else stringResource(
                        id = R.string.difficulty_star_count, levelSet.name, count
                    )
                Text(text = text, fontSize = 18.sp)
                if (levelSet != LevelSet.CUSTOM) Icon(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = stringResource(R.string.total_difficulty_star_count),
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .scale(.5f)
                )
            }
        }
    )
}


@Composable
fun LevelsList(
    navController: NavController,
    levelSet: LevelSet,
    levels: List<Level>,
    progress: List<Int>,
    editLevel: (Int?) -> Unit = {},
    deleteLevel: KFunction2<Int, String, Unit>? = null
) {
    val state = rememberScrollState()
    LaunchedEffect(key1 = progress) {
        state.scrollTo(state.maxValue)
    }

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(state)
            .testTag(stringResource(R.string.levels))
    ) {
        var nextLevel = progress.indexOf(0)
        if (nextLevel == -1) nextLevel = levels.size - 1
        val currentLevels = levels.subList(0, nextLevel + 1)
        currentLevels.forEachIndexed { index, level ->
            val stars = if (progress.isEmpty()) -1 else progress[index]
            LevelCard(navController, levelSet, level, stars, editLevel, deleteLevel)
            if (index == currentLevels.size - 1) Spacer(Modifier.height(1.dp))
            else Spacer(Modifier.height(25.dp))
        }
    }
}

@Composable
private fun LevelCard(
    navController: NavController,
    levelSet: LevelSet,
    level: Level,
    stars: Int,
    editLevel: (Int?) -> Unit = {},
    deleteLevel: KFunction2<Int, String, Unit>? = null
) {
    Card(
        border = BorderStroke(5.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .testTag(stringResource(id = R.string.level_card))
            .padding(15.dp)
            .clickable {
                if (levelSet == LevelSet.CUSTOM) navController.navigate("customLevelPlayer/${level.id}")
                else navController.navigate("level/${levelSet.name}/${level.id}")
            }
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(
                    shape = RectangleShape,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.secondaryContainer,
                            MaterialTheme.colorScheme.primaryContainer,
                        )
                    )
                )
                .padding(15.dp)
                .fillMaxWidth()
        ) {
            if (level.levelSet == LevelSet.CUSTOM) {
                BaseHeader(
                    startIcon = Icons.Filled.Edit,
                    endIcon = Icons.Filled.Delete,
                    withBorder = false,
                    middleContent = {
                        Text(
                            text = level.name.uppercase(Locale("us")),
                            fontSize = 32.sp,
                            fontStyle = FontStyle.Italic
                        )
                    },
                    startIconOnclick = {
                        editLevel(level.id)
                    },
                    endIconOnclick = {
                        if (deleteLevel != null) {
                            deleteLevel(level.id, level.name)
                        }
                    }
                )
            } else
                Text(
                    text = level.name.uppercase(Locale("us")),
                    fontSize = 32.sp,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

            Spacer(modifier = Modifier.height(20.dp))

            LevelPicture(level.x, level.initialBlocks)

            if (level.levelSet != LevelSet.CUSTOM) {
                Spacer(modifier = Modifier.height(20.dp))
                LevelStars(
                    result = stars, modifier = Modifier
                        .padding(15.dp)
                        .testTag(stringResource(id = R.string.level_stars, level.name))
                )
            }
        }
    }
}

@Composable
fun LevelPicture(
    x: Int,
    blocks: List<Char>,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(x),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.SpaceEvenly,
        userScrollEnabled = false,
        modifier = Modifier
            .height(335.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(
                width = 1.dp,
                color = Color.Black,
                RoundedCornerShape(2.dp)
            )
            .padding(top = 5.dp, bottom = 10.dp)
            .testTag(stringResource(R.string.level))
    ) {
        val scale = if (x == 4) 48.dp else 40.dp
        items(blocks.size) { index ->
            when (blocks[index]) {
                ENEMY_BLOCK -> {
                    EnemyBlock(size = scale)
                }

                PLAYER_BLOCK -> {
                    PlayerBlock(size = scale)
                }

                MOVABLE_BLOCK -> {
                    MovableBlock(size = scale)
                }

                GOAL_BLOCK -> {
                    GoalBlock(size = scale)
                }

                EMPTY_BLOCK -> {
                    EmptyBlock(size = scale)
                }

                UNMOVABLE_BLOCK -> {
                    UnmovableBlock(size = scale)
                }
            }
        }
    }
}


@Composable
private fun LevelStars(result: Int, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
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

enum class LevelSet {
    EASY,
    MEDIUM,
    HARD,
    CUSTOM
}
