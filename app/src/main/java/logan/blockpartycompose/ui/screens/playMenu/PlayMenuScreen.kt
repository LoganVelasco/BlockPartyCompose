package logan.blockpartycompose.ui.screens.playMenu

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController


@Composable
fun PlayMenuScreen(navController: NavController) {
    val viewModel: PlayMenuViewModel = hiltViewModel()
    val progress = viewModel.getProgress()

    PlayMenu(navController, progress)
}

@Composable
private fun PlayMenu(navController: NavController, progress: List<Int>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        MenuHeader(navController)
        MenuDifficulties(navController, progress)
    }
}

@Composable
private fun MenuHeader(navController: NavController) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Button(
            onClick = { navController.navigate("levelBuilder") },
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Level Builder",
                fontSize = 12.sp
            )
        }
        Spacer(Modifier.width(10.dp))
        Button(
            onClick = { navController.navigate("levels/custom") },
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "My Levels",
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun MenuDifficulties(navController: NavController, progress: List<Int>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(start = 20.dp, end = 20.dp)
    ) {
        DifficultyButton("Easy", progress[0]) { navController.navigate("easy") }
        DifficultyButton("Medium", progress[1]) { navController.navigate("medium") }
        DifficultyButton("Hard", progress[2]) { navController.navigate("hard") }
    }
}

@Composable
private fun DifficultyButton(text: String, progress: Int, onClick: () -> Unit ) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "$progress/30 Stars Collected", modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(5.dp))
        MenuStars(progress)
        Spacer(modifier = Modifier.height(5.dp))
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = text)
        }
    }
}

@Composable
private fun MenuStars(progress: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        if(progress >= 10){
            FilledStar()
        }else EmptyStar()
        if(progress >= 20) {
            FilledStar()
        }else EmptyStar()
        if(progress >= 30){
            FilledStar()
        }else EmptyStar()
    }
}

@Composable
fun EmptyStar() {
    Icon(
        Icons.Outlined.Star, contentDescription = "Star", tint = Color.Gray,
        modifier = Modifier
            .padding(5.dp)
            .scale(2.5f)
    )
}

@Composable
fun FilledStar() {
    Icon(
        Icons.Filled.Star, contentDescription = "Star",
        modifier = Modifier
            .padding(5.dp)
            .scale(2.5f)
    )
}

