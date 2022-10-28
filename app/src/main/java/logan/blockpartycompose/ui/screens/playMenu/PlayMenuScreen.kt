package logan.blockpartycompose.ui.screens.playMenu

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import logan.blockpartycompose.R
import logan.blockpartycompose.ui.components.BaseHeader
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet

@Composable
fun PlayMenuScreen(navController: NavController) {
    val viewModel: PlayMenuViewModel = hiltViewModel()

    val progress = remember {
        mutableStateOf(viewModel.getProgress())
    }

    PlayMenu(navController, progress) { viewModel.tempEdt() }

}

@Composable
private fun PlayMenu(
    navController: NavController,
    progress: MutableState<List<Int>>,
    function: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        MenuHeader(navController, progress.value.sum(), function)
        MenuDifficulties(navController, progress.value)
        MenuFooter(navController)
    }
}

@Composable
fun MenuHeader(navController: NavController, totalStars: Int, function: () -> Unit) {
    BaseHeader(
        firstIcon = Icons.Filled.Person,
        endIcon = Icons.Filled.Settings,
        middleContent = {
            Row(Modifier.padding(10.dp)) {
                Text(text = stringResource(id = R.string.total_star_progress, totalStars), fontSize = 18.sp)
                Icon(
                    Icons.Filled.Star, contentDescription = stringResource(R.string.total_star_count), modifier = Modifier
                        .scale(1.25f)
                        .padding(start = 5.dp)
                )
            }
        },
        firstIconOnclick = { navController.navigateUp() },
        endIconOnclick = function
    )
}

@Composable
private fun MenuFooter(navController: NavController) {
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
                text = stringResource(R.string.level_builder),
                fontSize = 12.sp
            )
        }
        Spacer(Modifier.width(10.dp))
        Button(
            onClick = { navController.navigate("custom") },
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(id = R.string.my_levels),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun MenuDifficulties(
    navController: NavController,
    progress: List<Int>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.6f)
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
            progress.sum(),
            ) { navController.navigate("medium") }
        DifficultyButton(
            LevelSet.HARD,
            progress[2],
            progress.sum(),
            ) { navController.navigate("hard") }
    }
}

@Composable
private fun DifficultyButton(
    difficulty: LevelSet,
    progress: Int,
    totalStars: Int,
    onClick: () -> Unit,
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
    difficulty: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.difficulty_star_progress, progress),
            modifier = Modifier.testTag(stringResource(id = R.string.difficulty_text, difficulty))
        )
        Spacer(modifier = Modifier.height(5.dp))
        Spacer(modifier = Modifier.height(5.dp))
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = difficulty)
        }
    }
}

@Composable
private fun DisabledDifficulty(
    requirement: Int,
    text: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.collection_requirement, requirement)
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
}




