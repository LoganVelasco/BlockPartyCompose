package logan.blockpartycompose.feature.playMenu

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import logan.blockpartycompose.MainActivity
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

            onNodeWithText("EASY").assertIsDisplayed()
            onNodeWithText("MEDIUM").assertIsDisplayed()
            onNodeWithText("HARD").assertIsDisplayed()

            onNodeWithText("EASY").assertIsEnabled()
            onNodeWithText("MEDIUM").assertIsNotEnabled()
            onNodeWithText("HARD").assertIsNotEnabled()

            onNodeWithText("0/30 Stars Collected").assertIsDisplayed()
            onNodeWithText("0/90 Total").assertIsDisplayed()
        }
    }

    @Test
    fun displaysEnabledMediumButton_WhenProgressIs15() {
        composeTestRule.apply {
            setProgress(activity, LevelSet.EASY, "3333300000")
            openPlayMenu()

            onNodeWithText("EASY").assertIsEnabled()
            onNodeWithText("MEDIUM").assertIsEnabled()
            onNodeWithText("HARD").assertIsNotEnabled()

            onNodeWithText("15/90 Total").assertIsDisplayed()

            onNodeWithTag("EASY text").assertTextEquals("15/30 Stars Collected")
            onNodeWithTag("MEDIUM text").assertTextEquals("0/30 Stars Collected")
        }
    }

    @Test
    fun displaysEnabledHardButton_WhenProgressIs30() {
        composeTestRule.apply {
            setProgress(activity, LevelSet.EASY, "3333333333")
            openPlayMenu()

            onNodeWithText("EASY").assertIsEnabled()
            onNodeWithText("MEDIUM").assertIsEnabled()
            onNodeWithText("HARD").assertIsEnabled()

            onNodeWithText("30/90 Total").assertIsDisplayed()

            onNodeWithTag("EASY text").assertTextEquals("30/30 Stars Collected")
            onNodeWithTag("MEDIUM text").assertTextEquals("0/30 Stars Collected")
            onNodeWithTag("HARD text").assertTextEquals("0/30 Stars Collected")
        }
    }

    @Test
    fun displaysTotalStarCounts() {
        composeTestRule.apply {
            setProgress(activity, LevelSet.EASY, "3333333330")
            setProgress(activity, LevelSet.MEDIUM, "3333333300")
            setProgress(activity, LevelSet.HARD, "3333333000")
            openPlayMenu()

            onNodeWithText("72/90 Total").assertIsDisplayed()

            onNodeWithTag("EASY text").assertTextEquals("27/30 Stars Collected")
            onNodeWithTag("MEDIUM text").assertTextEquals("24/30 Stars Collected")
            onNodeWithTag("HARD text").assertTextEquals("21/30 Stars Collected")
        }
    }
}