package logan.blockpartycompose.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import logan.blockpartycompose.ui.screens.levelsMenu.LevelsMenu
import logan.blockpartycompose.ui.screens.playMenu.PlayMenu
import logan.blockpartycompose.ui.screens.welcomeScreen.WelcomeScreen
import logan.blockpartycompose.ui.screens.level.LevelController
import logan.blockpartycompose.ui.screens.levelBuilder.CustomLevelPlayer
import logan.blockpartycompose.ui.screens.levelBuilder.LevelBuilderScreen
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet.*


@ExperimentalFoundationApi
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") { WelcomeScreen(navController) }
        composable("playMenu") { PlayMenu(navController) }
        composable("levelBuilder") { LevelBuilderScreen(navController) }
        composable("easy") { LevelsMenu(navController, EASY) }
        composable("medium") { LevelsMenu(navController, MEDIUM) }
        composable("hard") { LevelsMenu(navController, HARD) }
        composable("customLevel") { CustomLevelPlayer(navController) }
        composable("levels/{levelSet}") {
            LevelsMenu(navController,
                LevelSet.valueOf(it.arguments?.getString("levelSet")!!)
            )
        }
        composable("level/{levelSet}/{name}") {
            LevelController(
                navController,
                LevelSet.valueOf(it.arguments?.getString("levelSet")!!),
                it.arguments?.getString("name")!!
            )
        }
    }
}
