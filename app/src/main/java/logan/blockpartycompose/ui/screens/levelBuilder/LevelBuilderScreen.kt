package logan.blockpartycompose.ui.screens.levelBuilder

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import logan.blockpartycompose.R
import logan.blockpartycompose.data.models.BlockType
import logan.blockpartycompose.ui.components.*
import logan.blockpartycompose.ui.screens.level.LevelGrid
import logan.blockpartycompose.ui.theme.MainFont
import kotlin.reflect.KFunction2

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalFoundationApi
@Composable
fun LevelBuilderScreen(
    navigation: NavController,
    id: Int = -1,
    viewModel: LevelBuilderViewModel = hiltViewModel()
) {

    val state by viewModel.state.observeAsState()
    val context = LocalContext.current

    val dismissLevel = {
        viewModel.clearAllClicked()
        navigation.popBackStack(route = "playMenu", inclusive = false)
    }

    val backClicked = {
        if (viewModel.isInProgress()) {
            viewModel.showPopUpDialog()
        } else {
            dismissLevel()
        }
    }

    BackHandler {
        backClicked()
    }

    if (state == null) {
        if (id == -1) viewModel.setupNewLevel()
        else viewModel.setupExistingLevel(id, context)
        return
    }

    LevelBuilder(
        x = viewModel.level.x,
        blocks = state!!.blocks,
        selectedBlockType = state!!.selectedBlockType,
        backClicked = { backClicked() },
        blockClicked = viewModel::blockClicked,
        colorClicked = viewModel::colorSelected,
        undoClicked = viewModel::undoClicked,
        playClicked = {
            viewModel.playClicked()
            navigation.navigate("customLevelPlayer")
        },
        saveClicked = {
            viewModel.playClicked() // Refactor this
            viewModel.triggerSaveDialog()
        },
        clearAllClicked = viewModel::clearAllClicked
    )

    if (state?.showDialog == true) {
        UnsavedLevelDialog(
            dismissLevel = { dismissLevel() },
            saveLevel = { viewModel.triggerSaveDialog() }
        )
        return
    }

    if (state?.saved == true) {
        if (state?.isEdit == true) {
            SaveExistingLevelDialog(
                name = viewModel.level.name,
                closeDialog = { viewModel.hidePopUpDialog() },
                saveNewLevel = { viewModel.saveNewLevel() },
                saveLevel = {
                    viewModel.saveClicked(context)
                    viewModel.hidePopUpDialog()
                }
            )
        } else
            SaveLevelDialog(
                context,
                closeDialog = { viewModel.hidePopUpDialog() },
                saveLevel = viewModel::saveClicked
            )
    }

}


@Composable
fun UnsavedLevelDialog(dismissLevel: () -> Unit, saveLevel: () -> Unit) {
    AlertDialog(
        onDismissRequest = dismissLevel,
        title = {
            Text(text = stringResource(R.string.save_level_question), fontFamily = MainFont,)
        },
        text = {
            Text(
                stringResource(R.string.level_not_saved), fontFamily = MainFont,
            )
        },
        confirmButton =
        {
            Button(
                onClick = saveLevel
            ) {
                Text(stringResource(R.string.save), fontFamily = MainFont,)
            }
        },
        dismissButton =
        {
            Button(
                onClick = dismissLevel
            ) {
                Text(stringResource(R.string.discard), fontFamily = MainFont,)
            }
        }
    )
}

@ExperimentalMaterial3Api
@Composable
fun SaveLevelDialog(
    context: Context,
    closeDialog: () -> Unit,
    saveLevel: KFunction2<Context, String, Unit>,
) {
    val levelName = remember { mutableStateOf("") }
    val maxChar = 9
    val focusRequester = remember { FocusRequester() }
    AlertDialog(
        onDismissRequest = closeDialog,
        title = {
            Text(text = stringResource(id = R.string.save_level), fontFamily = MainFont,)
        },
        text = {
            Text(text = stringResource(R.string.enter_level_name), fontFamily = MainFont,)
        },
        confirmButton =
        {
            Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)) {
                TextField(
                    value = levelName.value,
                    onValueChange = { if (it.length <= maxChar) levelName.value = it },
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .testTag(stringResource(R.string.level_name))
                )
                Text(
                    text = "${levelName.value.length} / $maxChar",
                    textAlign = TextAlign.End,
                    fontFamily = MainFont,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = closeDialog
                    ) {
                        Text(stringResource(R.string.cancel), fontFamily = MainFont,)
                    }
                    Button(
                        enabled = levelName.value.isNotEmpty(),
                        onClick = {
                            saveLevel(context, levelName.value)
                            closeDialog()
                        }
                    ) {
                        Text(stringResource(id = R.string.save), fontFamily = MainFont,)
                    }
                }
            }
        }
    )
}

@Composable
fun SaveExistingLevelDialog(
    name: String,
    closeDialog: () -> Unit,
    saveNewLevel: () -> Unit,
    saveLevel: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = closeDialog,
        title = {
            Text(text = stringResource(R.string.override_existing_level, name), fontFamily = MainFont,)
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
                        onClick = saveLevel,
                        modifier = Modifier.weight(.45f)
                    ) {
                        Text(stringResource(R.string.override), fontFamily = MainFont,)
                    }
                    Spacer(modifier = Modifier.weight(.1f))
                    Button(
                        onClick = saveNewLevel,
                        modifier = Modifier.weight(.45f)
                    ) {
                        Text(stringResource(R.string.save), fontFamily = MainFont,)
                    }
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = closeDialog
                ) {
                    Text(stringResource(R.string.cancel), fontFamily = MainFont,)
                }
            }
        }
    )
}

@ExperimentalFoundationApi
@Composable
fun LevelBuilder(
    x: Int,
    blocks: List<Char>,
    selectedBlockType: BlockType?,
    blockClicked: (Char, Int) -> Unit,
    colorClicked: (BlockType) -> Unit,
    backClicked: () -> Unit,
    undoClicked: () -> Unit,
    playClicked: () -> Unit,
    saveClicked: () -> Unit,
    clearAllClicked: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            BackIcon(
                backClicked = { backClicked() },
                modifier = Modifier.testTag(stringResource(R.string.back_button))
            )
            Button(onClick = { clearAllClicked() }, Modifier.padding(15.dp)) {
                Text(text = stringResource(R.string.clear_all), fontFamily = MainFont,)
            }
        }
        LevelGrid(blockClicked = blockClicked, x = x, blocks = blocks)
        BlockPalette(selectedBlockType, colorClicked)
        LevelBuilderFooter(undoClicked, playClicked, saveClicked)
    }
}

@ExperimentalFoundationApi
@Composable
fun BlockPalette(selectedBlockType: BlockType?, typeClicked: (BlockType) -> Unit) {
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        LazyVerticalGrid( // refactor to Row
            columns = GridCells.Adaptive(60.dp),
            contentPadding = PaddingValues(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .scrollable(
                    state = ScrollableState { 0f },
                    orientation = Orientation.Vertical,
                    enabled = false
                )
                .testTag(stringResource(R.string.palette))
        ) {
            item {
                PlayerBlock(
                    onClick = { typeClicked(BlockType.PLAYER) },
                    isSelected = selectedBlockType == BlockType.PLAYER
                )
            }
            item {
                EnemyBlock(
                    onClick = { typeClicked(BlockType.ENEMY) },
                    isSelected = selectedBlockType == BlockType.ENEMY
                )
            }
            item {
                GoalBlock(
                    onClick = { typeClicked(BlockType.GOAL) },
                    isSelected = selectedBlockType == BlockType.GOAL
                )
            }
            item {
                MovableBlock(
                    onClick = { typeClicked(BlockType.MOVABLE) },
                    isSelected = selectedBlockType == BlockType.MOVABLE
                )
            }
            item {
                UnmovableBlock(
                    onClick = { typeClicked(BlockType.UNMOVABLE) },
                    isSelected = selectedBlockType == BlockType.UNMOVABLE
                )
            }
            item {
                EmptyBlock(
                    onClick = { typeClicked(BlockType.EMPTY) },
                    isSelected = selectedBlockType == BlockType.EMPTY
                )
            }
        }
    }
}

@Composable
fun LevelBuilderFooter(undoClicked: () -> Unit, playClicked: () -> Unit, saveClicked: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = { undoClicked() },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_undo_24),
                contentDescription = stringResource(R.string.undo),
            )
        }
        IconButton(
            onClick = { playClicked() },
        ) {
            Icon(Icons.Filled.PlayArrow, contentDescription = stringResource(R.string.play))
        }
        IconButton(
            onClick = { saveClicked() },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_save_24),
                contentDescription = stringResource(R.string.save),
            )
        }
    }
}