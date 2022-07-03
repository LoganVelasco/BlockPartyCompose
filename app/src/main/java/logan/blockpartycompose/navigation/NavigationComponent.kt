package logan.blockpartycompose.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import logan.blockpartycompose.ui.screens.playMenu.PlayMenuScreen
import logan.blockpartycompose.ui.screens.welcomeScreen.WelcomeScreen
import logan.blockpartycompose.ui.screens.level.LevelScreen
import logan.blockpartycompose.ui.screens.level.CustomLevelScreen
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
        composable("easy") { LevelsMenuScreen(navController, EASY) }
        composable("medium") { LevelsMenuScreen(navController, MEDIUM) }
        composable("hard") { LevelsMenuScreen(navController, HARD) }
        composable("customLevel") { CustomLevelScreen(navController) }
        composable("levels/{levelSet}") {
            LevelsMenuScreen(
                navController,
                LevelSet.valueOf(it.arguments?.getString("levelSet")!!)
            )
        }
        composable("level/{levelSet}/{name}") {
            LevelController(
                navController,
                LevelSet.valueOf(it.arguments?.getString("levelSet")!!),
                it.arguments?.getString("name")!!.toInt()
            )
        }
    }
}
