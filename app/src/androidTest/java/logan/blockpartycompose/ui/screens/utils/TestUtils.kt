package logan.blockpartycompose.ui.screens.utils

import android.content.Context
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
    this.onNodeWithText("Play").performClick()
    this.onNodeWithText(levelSet.name).performClick()
}

fun ComposeContentTestRule.openLevel(level: String = "LEVEL 1", levelSet: LevelSet = LevelSet.EASY) {
    this.onNodeWithText("Play").performClick()
    this.onNodeWithText(levelSet.name).performClick()
    this.onNodeWithText(level).performClick()
}

fun setProgress(
    activity: MainActivity,
    levelSet: LevelSet = LevelSet.EASY,
    progress: String = "0000000000"
) {
        activity.getSharedPreferences("levels", Context.MODE_PRIVATE).edit().putString(levelSet.name, progress)
        .apply()
}

fun clearProgress(activity: MainActivity) {
        activity.getSharedPreferences("levels", Context.MODE_PRIVATE).edit().clear()
        .apply()
}
