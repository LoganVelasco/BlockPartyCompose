package logan.blockpartycompose.feature.playMenu

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import logan.blockpartycompose.MainActivity
import logan.blockpartycompose.R
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import logan.blockpartycompose.utils.clearProgress
import logan.blockpartycompose.utils.openPlayMenu
import logan.blockpartycompose.utils.setProgress

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class PlayMenuScreenFeatureTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        clearProgress(composeTestRule.activity)
    }

    @Test
    fun clickingPlay_DisplaysInitialPlayMenuScreen_WhenNoProgress() {
        composeTestRule.apply {
            openPlayMenu()

            onNodeWithText(activity.getString(R.string.easy)).assertIsDisplayed()
            onNodeWithText(activity.getString(R.string.medium)).assertIsDisplayed()
            onNodeWithText(activity.getString(R.string.hard)).assertIsDisplayed()

            onNodeWithText(activity.getString(R.string.easy)).assertIsEnabled()
            onNodeWithText(activity.getString(R.string.medium)).assertIsNotEnabled()
            onNodeWithText(activity.getString(R.string.hard)).assertIsNotEnabled()

            onNodeWithText(activity.getString(R.string.difficulty_star_progress, 0)).assertIsDisplayed()
            onNodeWithText(activity.getString(R.string.total_star_progress, 0)).assertIsDisplayed()
        }
    }

    @Test
    fun displaysEnabledMediumButton_WhenProgressIs15() {
        composeTestRule.apply {
            setProgress(activity, LevelSet.EASY, "3333300000")
            openPlayMenu()

            onNodeWithText(activity.getString(R.string.easy)).assertIsEnabled()
            onNodeWithText(activity.getString(R.string.medium)).assertIsEnabled()
            onNodeWithText(activity.getString(R.string.hard)).assertIsNotEnabled()

            onNodeWithText(activity.getString(R.string.total_star_progress, 15)).assertIsDisplayed()

            onNodeWithTag(activity.getString(R.string.difficulty_text, "EASY")).assertTextEquals(activity.getString(R.string.difficulty_star_progress, 15))
            onNodeWithTag(activity.getString(R.string.difficulty_text, "MEDIUM")).assertTextEquals(activity.getString(R.string.difficulty_star_progress, 0))
        }
    }

    @Test
    fun displaysEnabledHardButton_WhenProgressIs30() {
        composeTestRule.apply {
            setProgress(activity, LevelSet.EASY, "3333333333")
            openPlayMenu()

            onNodeWithText(activity.getString(R.string.easy)).assertIsEnabled()
            onNodeWithText(activity.getString(R.string.medium)).assertIsEnabled()
            onNodeWithText(activity.getString(R.string.hard)).assertIsEnabled()

            onNodeWithText(activity.getString(R.string.total_star_progress, 30)).assertIsDisplayed()

            onNodeWithTag(activity.getString(R.string.difficulty_text, "EASY")).assertTextEquals(activity.getString(R.string.difficulty_star_progress, 30))
            onNodeWithTag(activity.getString(R.string.difficulty_text, "MEDIUM")).assertTextEquals(activity.getString(R.string.difficulty_star_progress, 0))
            onNodeWithTag(activity.getString(R.string.difficulty_text, "HARD")).assertTextEquals(activity.getString(R.string.difficulty_star_progress, 0))
        }
    }

    @Test
    fun displaysTotalStarCounts() {
        composeTestRule.apply {
            setProgress(activity, LevelSet.EASY, "3333333330")
            setProgress(activity, LevelSet.MEDIUM, "3333333300")
            setProgress(activity, LevelSet.HARD, "3333333000")
            openPlayMenu()

            onNodeWithText(activity.getString(R.string.total_star_progress, 72)).assertIsDisplayed()

            onNodeWithTag(activity.getString(R.string.difficulty_text, "EASY")).assertTextEquals(activity.getString(R.string.difficulty_star_progress, 27))
            onNodeWithTag(activity.getString(R.string.difficulty_text, "MEDIUM")).assertTextEquals(activity.getString(R.string.difficulty_star_progress, 24))
            onNodeWithTag(activity.getString(R.string.difficulty_text, "HARD")).assertTextEquals(activity.getString(R.string.difficulty_star_progress, 21))
        }
    }
}