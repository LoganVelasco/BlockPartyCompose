package logan.blockpartycompose.ui.screens.playMenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import logan.blockpartycompose.R
import logan.blockpartycompose.ui.components.BaseHeader
import logan.blockpartycompose.ui.components.TutorialPlayMenuWindow
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import logan.blockpartycompose.ui.theme.MainFont
import logan.blockpartycompose.utils.GameUtils.Companion.HARD_MAX_STARS
import logan.blockpartycompose.utils.GameUtils.Companion.HARD_STAR_REQUIREMENT
import logan.blockpartycompose.utils.GameUtils.Companion.MEDIUM_MAX_STARS
import logan.blockpartycompose.utils.GameUtils.Companion.MEDIUM_STAR_REQUIREMENT

@Composable
fun PlayMenuScreen(navController: NavController) {
    val viewModel: PlayMenuViewModel = hiltViewModel()
    val progress = viewModel.getProgress()
    val isHintsEnabled = viewModel.isHintsEnabled()

    PlayMenu(navController, progress, isHintsEnabled)
}

@Composable
private fun PlayMenu(
    navController: NavController,
    progress: List<Int>,
    isHintsEnabled: Boolean,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        MenuHeader(navController, progress.sum())
        MenuDifficulties(navController, progress)
        if (isHintsEnabled) TutorialPlayMenuWindow()
        MenuFooter(navController)
    }
}

@Composable
fun MenuHeader(navController: NavController, totalStars: Int) {
    BaseHeader(
        middleContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.total_star_progress, totalStars),
                    fontSize = 18.sp,
                    fontFamily = MainFont,
                    textAlign = TextAlign.Center,
                )
                Icon(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = stringResource(R.string.total_star_count),
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .scale(.5f)
                )
            }
        },
        startIcon = Icons.AutoMirrored.Filled.ArrowBack,
        endIcon = Icons.Filled.Settings,
        startIconOnclick = { navController.popBackStack() },
        endIconOnclick = { navController.navigate("settings") }
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
                fontFamily = MainFont,
                fontSize = 16.sp
            )
        }
        Spacer(Modifier.width(10.dp))
        Button(
            onClick = { navController.navigate("custom") },
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(id = R.string.my_levels),
                fontFamily = MainFont,
                fontSize = 16.sp
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
        verticalArrangement = Arrangement.SpaceAround,
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
                if (totalStars >= MEDIUM_STAR_REQUIREMENT) EnabledDifficulty(
                    progress,
                    onClick,
                    difficulty.name,
                    MEDIUM_MAX_STARS
                )
                else DisabledDifficulty(requirement = MEDIUM_STAR_REQUIREMENT, difficulty.name)
            }

            LevelSet.HARD -> {
                if (totalStars >= HARD_STAR_REQUIREMENT) EnabledDifficulty(
                    progress,
                    onClick,
                    difficulty.name,
                    HARD_MAX_STARS
                )
                else DisabledDifficulty(requirement = HARD_STAR_REQUIREMENT, difficulty.name)
            }

            LevelSet.CUSTOM -> {}
        }
    }
}

@Composable
private fun EnabledDifficulty(
    progress: Int,
    onClick: () -> Unit,
    difficulty: String,
    maxStarCount: Int = 30
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.difficulty_star_progress, progress, maxStarCount),
            fontFamily = MainFont,
            modifier = Modifier.testTag(stringResource(id = R.string.difficulty_text, difficulty))
        )
        Spacer(modifier = Modifier.height(5.dp))
        Spacer(modifier = Modifier.height(5.dp))
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = difficulty, fontFamily = MainFont, fontSize = 18.sp)
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
            text = stringResource(id = R.string.collection_requirement, requirement),
            fontFamily = MainFont,
        )
        Spacer(modifier = Modifier.height(5.dp))
        Spacer(modifier = Modifier.height(5.dp))
        Button(
            enabled = false,
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = text, fontFamily = MainFont, fontSize = 18.sp)
        }
    }
}




