package logan.blockpartycompose.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import logan.blockpartycompose.ui.screens.playMenu.PlayMenuScreen
import logan.blockpartycompose.ui.screens.welcomeScreen.WelcomeScreen
import logan.blockpartycompose.ui.screens.customLevels.CustomLevelScreen
import logan.blockpartycompose.ui.screens.level.LevelController
import logan.blockpartycompose.ui.screens.levelBuilder.LevelBuilderScreen
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet.*
import logan.blockpartycompose.ui.screens.levelsMenu.LevelsMenuScreen


@ExperimentalFoundationApi
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") { WelcomeScreen(navController) }
        composable("playMenu") { PlayMenuScreen(navController) }
        composable("levelBuilder") { LevelBuilderScreen(navController) }
        composable("levelBuilder/{id}") { LevelBuilderScreen(navController,  it.arguments?.getString("id")!!.toInt()) }
        composable("easy") { LevelsMenuScreen(navController, EASY) }
        composable("medium") { LevelsMenuScreen(navController, MEDIUM) }
        composable("hard") { LevelsMenuScreen(navController, HARD) }
        composable("custom") { LevelsMenuScreen(navController, CUSTOM) }
        composable("customLevelPlayer") { CustomLevelScreen(navController, -1) }
        composable("levels/{levelSet}") {
            LevelsMenuScreen(
                navController,
                LevelSet.valueOf(it.arguments?.getString("levelSet")!!)
            )
        }
        composable("level/{levelSet}/{id}") {
            if (LevelSet.valueOf(it.arguments?.getString("levelSet")!!) == CUSTOM)
                CustomLevelScreen(
                    navController,
                    it.arguments?.getString("id")!!.toInt()
                )
            else
                LevelController(
                    navController,
                    LevelSet.valueOf(it.arguments?.getString("levelSet")!!),
                    it.arguments?.getString("id")!!.toInt()
                )
        }
    }
}
