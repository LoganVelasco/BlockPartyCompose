package logan.blockpartycompose.ui.screens.levelBuilder

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import logan.blockpartycompose.data.models.BlockColor
import logan.blockpartycompose.ui.components.*
import logan.blockpartycompose.ui.screens.level.BackIcon
import logan.blockpartycompose.ui.screens.level.LevelGrid
import kotlin.reflect.KFunction2

@ExperimentalFoundationApi
@Composable
fun LevelBuilderScreen(
    navigation: NavController,
    id: Int = -1,
    viewModel: LevelBuilderViewModel = hiltViewModel(navigation.getViewModelStoreOwner(navigation.graph.id))
) {
    BackHandler {
        if (viewModel.isInProgress()) {
            viewModel.showPopUpDialog()
        } else {
            viewModel.clearAllClicked()
            navigation.navigateUp()
        }
    }

    val state by viewModel.state.observeAsState()
    val context = LocalContext.current
    if (state == null && id != -1) viewModel.setupExistingLevel(id)
    else if (state == null) viewModel.setupNewLevel()
    else if (state?.showDialog == true) {
        UnsavedLevelDialog(
            dismissLevel = {
                viewModel.clearAllClicked()
                navigation.popBackStack(route = "playMenu", inclusive = false)
            },
            saveLevel = { viewModel.triggerSaveDialog() }
        )
    } else {
        LevelBuilder(
            x = viewModel.level.x,
            blocks = state!!.blocks,
            selectedBlockColor = state!!.selectedBlockColor,
            backClicked = {
                if (viewModel.isInProgress()) {
                    viewModel.showPopUpDialog()
                } else {
                    viewModel.clearAllClicked()
                    navigation.navigateUp()
                }
            },
            blockClicked = viewModel::blockClicked,
            colorClicked = viewModel::colorSelected,
            menuClicked = viewModel::menuClicked,
            playClicked = {
                viewModel.playClicked()
                navigation.navigate("customLevelPlayer")
            },
            saveClicked = {
                if (viewModel.isInProgress()) { // disable save when not valid and when = to previous save
                    viewModel.playClicked() // Refactor this
                    viewModel.triggerSaveDialog()
                }
            },
            clearAllClicked = viewModel::clearAllClicked
        )
        if (state?.saved == true) {
            SaveLevelDialog(
                context,
                closeDialog = {
                    viewModel.hidePopUpDialog()
                },
                saveLevel = viewModel::saveClicked
            )
        }
    }
}


@Composable
fun UnsavedLevelDialog(dismissLevel: () -> Unit, saveLevel: () -> Unit) {
    AlertDialog(
        onDismissRequest = dismissLevel,
        title = {
            Text(text = "Save Level?")
        },
        text = {
            Text(
                "You haven't saved this level"
            )
        },
        confirmButton =
        {
            Button(
                onClick = saveLevel
            ) {
                Text("Continue")
            }
        },
        dismissButton =
        {
            Button(
                onClick = dismissLevel
            ) {
                Text("Discard")
            }
        }
    )
}

//@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SaveLevelDialog(
    context: Context,
    closeDialog: () -> Unit,
    saveLevel: KFunction2<Context, String, Unit>
) {
    val levelName = remember { mutableStateOf("") }
//    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
//        delay(100)
//        keyboard?.show()
    }
    AlertDialog(
        onDismissRequest = closeDialog,
        title = {
            Text(text = "Save Level")
        },
        text = {
            Text(text = "Enter Level Name")
        },
        confirmButton =
        {
            Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)) {
                TextField(
                    value = levelName.value,
                    onValueChange = { levelName.value = it },
                    modifier = Modifier.focusRequester(focusRequester)
                )
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = closeDialog
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            saveLevel(context, levelName.value)
                            closeDialog()
                        }
                    ) {
                        Text("Save")
                    }
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
    selectedBlockColor: BlockColor?,
    blockClicked: (Char, Int) -> Unit,
    colorClicked: (BlockColor) -> Unit,
    backClicked: () -> Unit,
    menuClicked: () -> Unit,
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
            BackIcon(backClicked = { backClicked() })
            Button(onClick = { clearAllClicked() }, Modifier.padding(15.dp)) {
                Text(text = "Clear All")
            }
        }
        LevelGrid(blockClicked = blockClicked, x = x, blocks = blocks)
        BlockPalette(selectedBlockColor, colorClicked)
        LevelBuilderFooter(menuClicked, playClicked, saveClicked)
    }
}

@ExperimentalFoundationApi
@Composable
fun BlockPalette(selectedBlockColor: BlockColor?, colorClicked: (BlockColor) -> Unit) {
    LazyVerticalGrid( // refactor to Row
        cells = GridCells.Adaptive(60.dp),
        contentPadding = PaddingValues(5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            BlueBox(
                onClick = { colorClicked(BlockColor.BLUE) },
                selectedBlockColor == BlockColor.BLUE
            )
        }
        item {
            RedBox(onClick = { colorClicked(BlockColor.RED) },
                selectedBlockColor == BlockColor.RED)
        }
        item {
            YellowBox(
                onClick = { colorClicked(BlockColor.YELLOW) },
                selectedBlockColor == BlockColor.YELLOW
            )
        }
        item {
            GreenBox(
                onClick = { colorClicked(BlockColor.GREEN) },
                selectedBlockColor == BlockColor.GREEN
            )
        }
        item {
            BlackBox(
                onClick = { colorClicked(BlockColor.BLACK) },
                selectedBlockColor == BlockColor.BLACK
            )
        }
        item {
            GrayBox(
                onClick = { colorClicked(BlockColor.GRAY) },
                selectedBlockColor == BlockColor.GRAY
            )
        }
    }
}

@Composable
fun LevelBuilderFooter(menuClicked: () -> Unit, playClicked: () -> Unit, saveClicked: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = { menuClicked() },
        ) {
            Icon(Icons.Filled.Menu, contentDescription = "Menu")
        }
        IconButton(
            onClick = { playClicked() },
        ) {
            Icon(Icons.Filled.PlayArrow, contentDescription = "Play")
        }
        IconButton(
            onClick = { saveClicked() },
        ) {
            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Save")
        }
    }
}