package logan.blockpartycompose.ui.screens.utils

import android.content.Context
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import logan.blockpartycompose.MainActivity
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet

fun ComposeContentTestRule.waitUntilNodeCount(
    matcher: SemanticsMatcher,
    count: Int,
    timeoutMillis: Long = 1_000L
) {
    this.waitUntil(timeoutMillis) {
        this.onAllNodes(matcher).fetchSemanticsNodes().size == count
    }
}

fun ComposeContentTestRule.waitUntilExists(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = 1_000L
) {
    return this.waitUntilNodeCount(matcher, 1, timeoutMillis)
}

fun ComposeContentTestRule.waitUntilDoesNotExist(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = 1_000L
) {
    return this.waitUntilNodeCount(matcher, 0, timeoutMillis)
}

fun ComposeContentTestRule.openPlayMenu() {
    this.onNodeWithText("Play").performClick()
}

fun ComposeContentTestRule.openLevelMenu(levelSet: LevelSet = LevelSet.EASY) {
    this.openPlayMenu()
    this.onNodeWithText(levelSet.name).performClick()
}

fun ComposeContentTestRule.openLevel(level: Int = 1, levelSet: LevelSet = LevelSet.EASY) {
    this.openLevelMenu(levelSet)
    this.onNodeWithTag("levels").performScrollToIndex(level - 1)
    this.onNodeWithText("LEVEL $level").performClick()
}

fun ComposeContentTestRule.movePlayerRight() {
    this.onNodeWithTag("level").onChildren()[getPlayerIndex()+1].performClick()
}

fun ComposeContentTestRule.movePlayerLeft() {
    this.onNodeWithTag("level").onChildren()[getPlayerIndex()-1].performClick()
}

fun ComposeContentTestRule.movePlayerUp() {
    val x = if(this.onNodeWithTag("level").onChildren().fetchSemanticsNodes().count() <= 24) 4 else 6
    this.onNodeWithTag("level").onChildren()[getPlayerIndex()-x].performClick()
}

fun ComposeContentTestRule.movePlayerDown() {
    val x = if(this.onNodeWithTag("level").onChildren().fetchSemanticsNodes().count() <= 24) 4 else 6
    this.onNodeWithTag("level").onChildren()[getPlayerIndex()+x].performClick()
}

private fun ComposeContentTestRule.getPlayerIndex(): Int {
    var i = 0
    for (node in (this.onNodeWithTag("level").onChildren().fetchSemanticsNodes())) {
        if (node.config[SemanticsProperties.TestTag] != "player") {
            i++
        } else {
            break
        }
    }
    return i
}


fun setProgress(
    activity: MainActivity,
    levelSet: LevelSet = LevelSet.EASY,
    progress: String = "0000000000"
) {
    activity.getSharedPreferences("levels", Context.MODE_PRIVATE).edit()
        .putString(levelSet.name, progress)
        .apply()
}

fun clearProgress(activity: MainActivity) {
    activity.getSharedPreferences("levels", Context.MODE_PRIVATE).edit().clear()
        .apply()
}
