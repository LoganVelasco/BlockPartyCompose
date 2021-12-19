package logan.blockpartycompose.ui.screens.levelBuilder

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import logan.blockpartycompose.data.models.BlockColor
import logan.blockpartycompose.ui.components.*
import logan.blockpartycompose.ui.screens.level.BackIcon
import logan.blockpartycompose.ui.screens.level.LevelGrid

@ExperimentalFoundationApi
@Composable
fun LevelBuilder(
    onNavigate: (String) -> Unit,
    navigateUp: () -> Unit,
    navigationId: ViewModelStoreOwner,
    viewModel: LevelBuilderViewModel = hiltViewModel(navigationId)
) {
    val state by viewModel.state.observeAsState()
    val onClicks = viewModel.levelBuilderOnClicks

    if (state == null) {
        viewModel.setupNewLevel()
    } else {
        LevelBuilderScreen(
            state = state!!,
            onClicks = onClicks,
            onNavigate = onNavigate,
            navigateUp = navigateUp,
        )
    }
}


@ExperimentalFoundationApi
@Composable
fun LevelBuilderScreen(
    state: LevelBuilderViewModel.LevelBuilderState,
    onClicks: LevelBuilderViewModel.LevelBuilderOnClicks,
    onNavigate: (String) -> Unit,
    navigateUp: () -> Unit,
) {
    onClicks.apply {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            LevelBuilderHeader(
                navigateUp = navigateUp,
                backOnClicked = backOnClicked,
                clearAllOnClicked = clearAllOnClicked
            )
            LevelGrid(blockClicked = blockOnClicked, x = state.x, blocks = state.blocks)
            BlockPalette(
                selectedBlockColor = state.selectedBlockColor,
                colorClicked = colorOnClicked
            )
            LevelBuilderFooter(
                playClicked = {
                    playOnClicked()
                    onNavigate("customLevel")
                },
                menuClicked = menuOnClicked,
                saveClicked = saveOnClicked
            )
        }
    }
}

@Composable
fun LevelBuilderHeader(
    navigateUp: () -> Unit,
    backOnClicked: () -> Unit,
    clearAllOnClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        BackIcon(onClick = {
            // Todo: test if this call is needed
            backOnClicked()
            navigateUp()
        })
        Button(onClick = { clearAllOnClicked() }, Modifier.padding(15.dp)) {
            Text(text = "Clear All")
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun BlockPalette(selectedBlockColor: BlockColor?, colorClicked: (BlockColor) -> Unit) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(60.dp),
        contentPadding = PaddingValues(5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            BlueBox(
                onClick = { colorClicked(BlockColor.BLUE) },
                isSelected = (selectedBlockColor == BlockColor.BLUE)
            )
        }
        item {
            RedBox(
                onClick = { colorClicked(BlockColor.RED) },
                isSelected = (selectedBlockColor == BlockColor.RED)
            )
        }
        item {
            YellowBox(
                onClick = { colorClicked(BlockColor.YELLOW) },
                isSelected = (selectedBlockColor == BlockColor.YELLOW)
            )
        }
        item {
            GreenBox(
                onClick = { colorClicked(BlockColor.GREEN) },
                isSelected = (selectedBlockColor == BlockColor.GREEN)
            )
        }
        item {
            BlackBox(
                onClick = { colorClicked(BlockColor.BLACK) },
                isSelected = (selectedBlockColor == BlockColor.BLACK)
            )
        }
        item {
            GrayBox(
                onClick = { colorClicked(BlockColor.GRAY) },
                isSelected = (selectedBlockColor == BlockColor.GRAY)
            )
        }
    }
}

@Composable
fun LevelBuilderFooter(playClicked: () -> Unit, menuClicked: () -> Unit, saveClicked: () -> Unit) {
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