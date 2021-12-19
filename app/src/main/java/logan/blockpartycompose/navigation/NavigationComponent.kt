package logan.blockpartycompose.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import logan.blockpartycompose.ui.screens.level.Level
import logan.blockpartycompose.ui.screens.levelBuilder.CustomLevelPlayer
import logan.blockpartycompose.ui.screens.levelBuilder.LevelBuilder
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet.*
import logan.blockpartycompose.ui.screens.levelsMenu.LevelsMenu
import logan.blockpartycompose.ui.screens.mainMenu.MainMenu
import logan.blockpartycompose.ui.screens.splashScreen.SplashScreen

@ExperimentalFoundationApi
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "menus") {
        menusGraph(navController)
        levelBuilderGraph(navController)

        composable("level/{levelSet}/{name}") {
            Level(
                navigateUp = { navigateUp(navController) },
                levelSet = LevelSet.valueOf(it.arguments?.getString("levelSet")!!),
                name = it.arguments?.getString("name")!!
            )
        }
    }
}

fun NavGraphBuilder.menusGraph(navController: NavController) {
    navigation(startDestination = "welcome", route = "menus") {
        composable("welcome") { SplashScreen { navigateToScreen(navController, it) } }
        composable("playMenu") { MainMenu { navigateToScreen(navController, it) } }
        composable("easy") {
            LevelsMenu(
                navigateUp = { navigateUp(navController) },
                onNavigate = { navigateToScreen(navController, it) },
                levelSet = EASY
            )
        }
        composable("medium") {
            LevelsMenu(
                navigateUp = { navigateUp(navController) },
                onNavigate = { navigateToScreen(navController, it) },
                levelSet = MEDIUM
            )
        }
        composable("hard") {
            LevelsMenu(
                navigateUp = { navigateUp(navController) },
                onNavigate = { navigateToScreen(navController, it) },
                levelSet = HARD
            )
        }
        composable("levels/{levelSet}") {
            LevelsMenu(
                navigateUp = { navigateUp(navController) },
                onNavigate = { navigateToScreen(navController, it) },
                levelSet = LevelSet.valueOf(
                    it.arguments?.getString(
                        "levelSet"
                    )!!
                )
            )
        }
    }
}

@ExperimentalFoundationApi
fun NavGraphBuilder.levelBuilderGraph(navController: NavController) {
    navigation(startDestination = "levelBuilder", route = "levelBuilderGraph") {
        composable("levelBuilder") {
            LevelBuilder(
                navigateUp = { navigateUp(navController) },
                onNavigate = { navigateToScreen(navController, it) },
                navigationId = navController.getViewModelStoreOwner(navController.graph.id)
            )
        }
        composable("customLevel") {
            CustomLevelPlayer(
                navigateUp = { navigateUp(navController) },
                onNavigate = { navigateToScreen(navController, it) },
                navigationId = navController.getViewModelStoreOwner(navController.graph.id)
            )
        }
    }

}

private fun navigateToScreen(
    navController: NavController,
    screen: String
) {
    navController.navigate(screen)
}

private fun navigateUp(
    navController: NavController,
) {
    navController.navigateUp()
}


