package logan.blockpartycompose.ui.screens.level

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import logan.blockpartycompose.MainActivity
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import logan.blockpartycompose.ui.screens.utils.clearProgress
import logan.blockpartycompose.ui.screens.utils.openLevel
import logan.blockpartycompose.ui.screens.utils.waitUntilExists

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class LevelsScreenInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
            clearProgress(composeTestRule.activity)
    }

    @Test
    fun displaysCorrectLevel() {
        composeTestRule.apply {
            openLevel("LEVEL 1", LevelSet.EASY)

            onNodeWithTag("level").onChildren()[9].assert(hasTestTag("player"))
            onNodeWithTag("level").onChildren()[18].assert(hasTestTag("goal"))
            onNodeWithTag("level").onChildren().filter(hasTestTag("empty")).assertCountEquals(22)
        }
    }

    @Test
    fun displaysSuccessScreen_whenLevelCompleted() {
        composeTestRule.apply {
            openLevel("LEVEL 1", LevelSet.EASY)

            onNodeWithTag("level").onChildren()[10].performClick()
            onNodeWithTag("level").onChildren()[14].performClick()
            onNodeWithTag("level").onChildren()[18].performClick()

            waitUntilExists(hasText("You Did it!"), 1000)

            onNodeWithText("Level 1 Completed in 3 moves!").assertIsDisplayed()
        }
    }

}