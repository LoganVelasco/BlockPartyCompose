package logan.blockpartycompose.ui.screens.levelsMenu

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.ui.screens.level.BackIcon
import logan.blockpartycompose.ui.screens.level.LevelGrid

@Composable
fun LevelsMenuScreen(
    navController: NavController,
    levelSet: LevelSet,
){
    val viewModel:LevelsMenuViewModel = hiltViewModel()
    val levels = viewModel.getLevels(levelSet)

    LevelsList(navController, levelSet, levels)
}

@Composable
private fun LevelsList(
    navController: NavController,
    levelSet: LevelSet,
    levels: List<Level>,
) {
    Column {
        BackIcon(backClicked = { navController.navigateUp() }, modifier = Modifier.padding(10.dp))
        LazyColumn(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding()
        ) {
            item {
                Text(text = levelSet.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            items(levels.size) { index ->
                val level = levels[index]
                LevelCard(navController, levelSet, level)
            }
        }
    }
}

//@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LevelCard(
    navController: NavController,
    levelSet: LevelSet,
    level: Level
) {

//    LevelGrid(blockClicked = { _: Char, _: Int ->
//        run {
//            navController.navigate("level/${levelSet.name}/${level.name}")
//        }
//    },
//        x = level.x,
//        blocks = level.blocks
//    )
    Button(
        onClick = {
            navController.navigate("level/${levelSet.name}/${level.name}")
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp)
    ) {
        Text(text = level.name.toString())
    }
}

enum class LevelSet(string: String){
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard"),
    CUSTOM("Custom")
}
