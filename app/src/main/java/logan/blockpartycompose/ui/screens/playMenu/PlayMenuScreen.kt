package logan.blockpartycompose.ui.screens.playMenu

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet

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
            onClick = { navController.navigate("custom") },
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
        DifficultyButton(
            LevelSet.EASY,
            progress[0],
            progress.sum()
        ) { navController.navigate("easy") }
        DifficultyButton(
            LevelSet.MEDIUM,
            progress[1],
            progress.sum()
        ) { navController.navigate("medium") }
        DifficultyButton(
            LevelSet.HARD,
            progress[2],
            progress.sum()
        ) { navController.navigate("hard") }
    }
}

@Composable
private fun DifficultyButton(
    difficulty: LevelSet,
    progress: Int,
    totalStars: Int,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        when (difficulty) {
            LevelSet.EASY -> {
                EnabledDifficulty(progress, onClick, difficulty.name)
            }
            LevelSet.MEDIUM -> {
                if (totalStars >= 15) EnabledDifficulty(progress, onClick, difficulty.name)
                else DisabledDifficulty(requirement = 15, difficulty.name)
            }
            LevelSet.HARD -> {
                if (totalStars >= 30) EnabledDifficulty(progress, onClick, difficulty.name)
                else DisabledDifficulty(requirement = 30, difficulty.name)
            }
            LevelSet.CUSTOM -> {

            }
        }
    }
}

@Composable
private fun EnabledDifficulty(
    progress: Int,
    onClick: () -> Unit,
    text: String
) {
    Text(
        text = "$progress/30 Stars Collected"
    )
    Spacer(modifier = Modifier.height(5.dp))
    Spacer(modifier = Modifier.height(5.dp))
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = text)
    }
}

@Composable
private fun DisabledDifficulty(
    requirement: Int,
    text: String
) {
    Text(
        text = "Collect $requirement stars to unlock"
    )
    Spacer(modifier = Modifier.height(5.dp))
    Spacer(modifier = Modifier.height(5.dp))
    Button(
        enabled = false,
        onClick = {},
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = text)
    }
}




