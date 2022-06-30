package logan.blockpartycompose.ui.screens.levelsMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.ui.screens.level.BackIcon
import logan.blockpartycompose.ui.screens.playMenu.EmptyStar
import logan.blockpartycompose.ui.screens.playMenu.FilledStar

@Composable
fun LevelsMenuScreen(
    navController: NavController,
    levelSet: LevelSet,
){
    val viewModel:LevelsMenuViewModel = hiltViewModel()
    val levels = viewModel.getLevels(levelSet)
    val progress = viewModel.getProgress(levelSet)

    LevelsList(navController, levelSet, levels, progress)
}

@Composable
private fun LevelsList(
    navController: NavController,
    levelSet: LevelSet,
    levels: List<Level>,
    progress: List<Int>,
) {
    Column {
        BackIcon(backClicked = { navController.navigateUp() }, modifier = Modifier.padding(10.dp))
        LazyColumn(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding()
                .fillMaxSize()
        ) {
            item {
                Text(text = levelSet.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(50.dp))
            }
            items(levels.size) { index ->
                val level = levels[index]
                LevelCard(navController, levelSet, level, progress[index])
                Spacer(Modifier.height(25.dp))
            }
        }
    }
}

//@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LevelCard(
    navController: NavController,
    levelSet: LevelSet,
    level: Level,
    stars: Int
) {

//    LevelGrid(blockClicked = { _: Char, _: Int ->
//        run {
//            navController.navigate("level/${levelSet.name}/${level.name}")
//        }
//    },
//        x = level.x,
//        blocks = level.blocks
//    )
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(15.dp)
            .background(shape = RectangleShape, color = Color.White)
            .border(
                width = 2.dp,
                color = Color.DarkGray,
                RoundedCornerShape(2.dp)
            )// TODO Fix white on rounded corners
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Level ${level.name}")
//        Text(text = "Moves Used: 10")
        LevelStars(result = stars, modifier = Modifier.padding(15.dp))
        Button(
            onClick = {
                navController.navigate("level/${levelSet.name}/${level.name}")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 25.dp, bottom = 25.dp)
        ) {
            Text(text = "Play")
        }
    }
}

@Composable
private fun LevelStars(result: Int, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
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

enum class LevelSet(string: String){
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard"),
    CUSTOM("Custom")
}
