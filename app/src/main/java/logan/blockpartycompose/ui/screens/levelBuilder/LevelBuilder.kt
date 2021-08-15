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
import androidx.navigation.NavController
import logan.blockpartycompose.data.models.BlockColor
import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.ui.components.*
import logan.blockpartycompose.ui.screens.level.BackIcon
import logan.blockpartycompose.ui.screens.level.LevelGrid

@ExperimentalFoundationApi
@Composable
fun LevelBuilderScreen(
    navigation: NavController,
    viewModel: LevelBuilderViewModel = hiltViewModel()
) {
    val state by viewModel.state.observeAsState()
    if (state == null) viewModel.setupNewLevel()
    else
        LevelBuilder(
            navController = navigation,
            viewModel.level.x,
            state!!.blocks,
            state!!.selectedBlockColor,
            viewModel::blockClicked,
            viewModel::colorSelected
        )
}


@ExperimentalFoundationApi
@Composable
fun LevelBuilder(
    navController: NavController,
    x: Int,
    blocks: List<Char>,
    selectedBlockColor: BlockColor?,
    blockClicked: (Char, Int) -> Unit,
    colorClicked: (BlockColor) -> Unit
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
            BackIcon(backClicked = { navController.navigateUp() })
            Button(onClick = { /*TODO*/ }, Modifier.padding(15.dp)) {
                Text(text = "Clear All")
            }
        }
        LevelGrid(blockClicked = blockClicked, x = x, blocks = blocks)
        BlockPalette(selectedBlockColor, colorClicked)
        LevelBuilderFooter()
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
            BlueBox(onClick = { colorClicked(BlockColor.BLUE) })
        }
        item {
            RedBox(onClick = { colorClicked(BlockColor.RED) })
        }
        item {
            YellowBox(onClick = { colorClicked(BlockColor.YELLOW) })
        }
        item {
            GreenBox(onClick = { colorClicked(BlockColor.GREEN) })
        }
        item {
            BlackBox(onClick = { colorClicked(BlockColor.BLACK) })
        }
        item {
            GrayBox(onClick = { colorClicked(BlockColor.GRAY) })
        }
    }
}

@Composable
fun LevelBuilderFooter() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = { /*TODO*/ },
        ) {
            Icon(Icons.Filled.Menu, contentDescription = "Menu")
        }
        IconButton(
            onClick = { /*TODO*/ },
        ) {
            Icon(Icons.Filled.PlayArrow, contentDescription = "Play")
        }
        IconButton(
            onClick = { /*TODO*/ },
        ) {
            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Save")
        }
    }
}