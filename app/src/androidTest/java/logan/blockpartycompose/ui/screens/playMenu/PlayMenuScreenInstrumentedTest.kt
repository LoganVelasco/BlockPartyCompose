package logan.blockpartycompose.ui.screens.playMenu

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import logan.blockpartycompose.MainActivity
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class PlayMenuScreenInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        val prefs = composeTestRule.activity.getSharedPreferences("levels", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }

    @Test
    fun clickingPlay_DisplaysInitialPlayMenuScreen_WhenNoProgress() {

        composeTestRule.onNodeWithText("Play").performClick()

        composeTestRule.onNodeWithText("EASY").assertIsDisplayed()
        composeTestRule.onNodeWithText("MEDIUM").assertIsDisplayed()
        composeTestRule.onNodeWithText("HARD").assertIsDisplayed()

        composeTestRule.onNodeWithText("EASY").assertIsEnabled()
        composeTestRule.onNodeWithText("MEDIUM").assertIsNotEnabled()
        composeTestRule.onNodeWithText("HARD").assertIsNotEnabled()

        composeTestRule.onNodeWithText("0/30 Stars Collected").assertIsDisplayed()
        composeTestRule.onNodeWithText("0/90 Total").assertIsDisplayed()
    }

    @Test
    fun displaysEnabledMediumButton_WhenProgressIs15() {
        val prefs = composeTestRule.activity.getSharedPreferences("levels", Context.MODE_PRIVATE)
        prefs.edit().putString(LevelSet.EASY.name, "3333300000")
            .apply()

        composeTestRule.onNodeWithText("Play").performClick()

        composeTestRule.onNodeWithText("EASY").assertIsEnabled()
        composeTestRule.onNodeWithText("MEDIUM").assertIsEnabled()
        composeTestRule.onNodeWithText("HARD").assertIsNotEnabled()

        composeTestRule.onNodeWithText("15/90 Total").assertIsDisplayed()

        composeTestRule.onNodeWithTag("EASY text").assertTextEquals("15/30 Stars Collected")
        composeTestRule.onNodeWithTag("MEDIUM text").assertTextEquals("0/30 Stars Collected")
    }

    @Test
    fun displaysEnabledHardButton_WhenProgressIs30() {
        val prefs = composeTestRule.activity.getSharedPreferences("levels", Context.MODE_PRIVATE)
        prefs.edit().putString(LevelSet.EASY.name, "3333333333")
            .apply()

        composeTestRule.onNodeWithText("Play").performClick()

        composeTestRule.onNodeWithText("EASY").assertIsEnabled()
        composeTestRule.onNodeWithText("MEDIUM").assertIsEnabled()
        composeTestRule.onNodeWithText("HARD").assertIsEnabled()

        composeTestRule.onNodeWithText("30/90 Total").assertIsDisplayed()

        composeTestRule.onNodeWithTag("EASY text").assertTextEquals("30/30 Stars Collected")
        composeTestRule.onNodeWithTag("MEDIUM text").assertTextEquals("0/30 Stars Collected")
        composeTestRule.onNodeWithTag("HARD text").assertTextEquals("0/30 Stars Collected")
    }

    @Test
    fun displaysTotalStarCounts() {
        val prefs = composeTestRule.activity.getSharedPreferences("levels", Context.MODE_PRIVATE)
        prefs.edit().putString(LevelSet.EASY.name, "3333333330")
            .apply()
        prefs.edit().putString(LevelSet.MEDIUM.name, "3333333300")
            .apply()
        prefs.edit().putString(LevelSet.HARD.name, "3333333000")
            .apply()

        composeTestRule.onNodeWithText("Play").performClick()

        composeTestRule.onNodeWithText("72/90 Total").assertIsDisplayed()

        composeTestRule.onNodeWithTag("EASY text").assertTextEquals("27/30 Stars Collected")
        composeTestRule.onNodeWithTag("MEDIUM text").assertTextEquals("24/30 Stars Collected")
        composeTestRule.onNodeWithTag("HARD text").assertTextEquals("21/30 Stars Collected")
    }
}