package logan.blockpartycompose.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import logan.blockpartycompose.ui.screens.playMenu.PlayMenuScreen
import logan.blockpartycompose.ui.screens.splash.WelcomeScreen
import logan.blockpartycompose.ui.screens.customLevels.CustomLevelScreen
import logan.blockpartycompose.ui.screens.level.LevelController
import logan.blockpartycompose.ui.screens.levelBuilder.LevelBuilderScreen
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet.*
import logan.blockpartycompose.ui.screens.levelsMenu.LevelsMenuScreen
import logan.blockpartycompose.ui.screens.settings.SettingsScreen
import logan.blockpartycompose.ui.screens.tutorialMode.TutorialModeScreen


@ExperimentalFoundationApi
@Composable
fun Navigation(isUpdated: Boolean, resetApp: () -> Unit, closeApp:() -> Unit) {
    val navController = rememberNavController()
    val start = if(isUpdated) "refreshedSettings" else "welcome"
    NavHost(navController = navController, startDestination = start) {
        composable("welcome") { WelcomeScreen(navController, closeApp) }
        composable("tutorialMode") { TutorialModeScreen(navController) }
        composable("playMenu") { PlayMenuScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("updatedSettings") {
            resetApp()
        }
        composable("refreshedSettings") {
            navController.navigate("welcome")
            navController.navigate("playMenu")
            navController.navigate("settings")
        }
        composable("levelBuilder") { LevelBuilderScreen(navController) }
        composable("levelBuilder/{id}") { LevelBuilderScreen(navController,  it.arguments?.getString("id")!!.toInt()) }
        composable("easy") { LevelsMenuScreen(navController, EASY) }
        composable("medium") { LevelsMenuScreen(navController, MEDIUM) }
        composable("hard") { LevelsMenuScreen(navController, HARD) }
        composable("custom") { LevelsMenuScreen(navController, CUSTOM) }
        composable("customLevelPlayer") { CustomLevelScreen(navController) }
        composable("customLevelPlayer/{id}") { CustomLevelScreen(navController, it.arguments?.getString("id")!!.toInt()) }
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
