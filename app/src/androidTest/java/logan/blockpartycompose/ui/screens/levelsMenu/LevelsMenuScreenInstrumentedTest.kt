package logan.blockpartycompose.ui.screens.levelsMenu

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
class LevelsMenuScreenInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        val prefs = composeTestRule.activity.getSharedPreferences("levels", Context.MODE_PRIVATE)
        prefs.edit().putString(LevelSet.EASY.name, "3333333333")
            .apply()
    }

    @Test
    fun clickingEasy_DisplaysInitialEasyLevels() {
        composeTestRule.onNodeWithText("Play").performClick()
        composeTestRule.onNodeWithText("EASY").performClick()
        composeTestRule.onNodeWithText("EASY: 30/30").assertIsDisplayed()

    }
    @Test
    fun clickingMedium_DisplaysInitialMediumLevels() {
        composeTestRule.onNodeWithText("Play").performClick()
        composeTestRule.onNodeWithText("MEDIUM").performClick()
        composeTestRule.onNodeWithText("MEDIUM: 0/30").assertIsDisplayed()

    }
    @Test
    fun clickingHard_DisplaysInitialHardLevels() {
        composeTestRule.onNodeWithText("Play").performClick()
        composeTestRule.onNodeWithText("HARD").performClick()
        composeTestRule.onNodeWithText("HARD: 0/30").assertIsDisplayed()

    }
}