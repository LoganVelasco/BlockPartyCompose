package logan.blockpartycompose.ui.screens.levelsMenu

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.ui.components.EmptyStar
import logan.blockpartycompose.ui.components.FilledStar
import logan.blockpartycompose.ui.screens.level.BackIcon
import logan.blockpartycompose.ui.screens.level.LevelGrid

@Composable
fun LevelsMenuScreen(
    navController: NavController,
    levelSet: LevelSet,
) {
    val viewModel: LevelsMenuViewModel = hiltViewModel()
    val levels = viewModel.getLevels(levelSet, LocalContext.current)
    val progress = viewModel.getProgress(levelSet)

    LevelsList(navController, levelSet, levels, progress)
}

@Composable
fun LevelsList(
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
                val stars = if (progress.isEmpty())-1 else progress[index]
                LevelCard(navController, levelSet, level, stars)
                Spacer(Modifier.height(25.dp))
            }
        }
    }
}

@Composable
private fun LevelCard(
    navController: NavController,
    levelSet: LevelSet,
    level: Level,
    stars: Int
) {
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
        Text(text = level.name)
        if(stars <= 0)LevelStars(result = stars, modifier = Modifier.padding(15.dp))
        Button(
            onClick = {
                navController.navigate("level/${levelSet.name}/${level.id}")
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

enum class LevelSet(string: String) {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard"),
    CUSTOM("Custom")
}
