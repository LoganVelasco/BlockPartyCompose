package logan.blockpartycompose.feature.levelsMenu

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import logan.blockpartycompose.MainActivity
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import logan.blockpartycompose.utils.clearProgress
import logan.blockpartycompose.utils.openLevelMenu
import logan.blockpartycompose.utils.setProgress
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LevelsMenuScreenFeatureTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        clearProgress(composeTestRule.activity)
    }

    @Test
    fun clickingEasy_DisplaysInitialEasyLevels() {
        composeTestRule.apply {
            openLevelMenu(LevelSet.EASY)

            onNodeWithText("EASY: 0/30").assertIsDisplayed()
        }
    }

    @Test
    fun clickingEasy_DisplaysAllEasyLevels() {
        composeTestRule.apply {
            openLevelMenu(LevelSet.EASY)

            onNodeWithText("LEVEL 1").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 2").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 3").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 4").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 5").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 6").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 7").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 8").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 9").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 10").assertIsDisplayed()
        }
    }

    @Test
    fun clickingMedium_DisplaysInitialMediumLevels() {
        composeTestRule.apply {
            setProgress(activity, LevelSet.EASY, "3333333333")
            openLevelMenu(LevelSet.MEDIUM)

            composeTestRule.onNodeWithText("MEDIUM: 0/30").assertIsDisplayed()
        }
    }

    @Test
    fun clickingMedium_DisplaysAllMediumLevels() {
        composeTestRule.apply {
            setProgress(activity, LevelSet.EASY, "3333333333")
            openLevelMenu(LevelSet.MEDIUM)

            onNodeWithText("LEVEL 1").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 2").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 3").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 4").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 5").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 6").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 7").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 8").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 9").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 10").assertIsDisplayed()
        }
    }

    @Test
    fun clickingHard_DisplaysInitialHardLevels() {
        composeTestRule.apply {
            setProgress(activity, LevelSet.EASY, "3333333333")
            openLevelMenu(LevelSet.HARD)

            onNodeWithText("HARD: 0/30").assertIsDisplayed()
        }
    }

    @Test
    fun clickingHard_DisplaysAllHardLevels() {
        composeTestRule.apply {
            setProgress(activity, LevelSet.EASY, "3333333333")
            openLevelMenu(LevelSet.HARD)

            onNodeWithText("LEVEL 1").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 2").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 3").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 4").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 5").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 6").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 7").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 8").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 9").performTouchInput { swipeUp() }
            onNodeWithText("LEVEL 10").assertIsDisplayed()
        }
    }

    @Test
    fun displaysCorrectLevelProgress() {
        composeTestRule.apply {
            setProgress(activity = activity, levelSet = LevelSet.EASY, progress = "3210000000")

            openLevelMenu(LevelSet.EASY)

            onNodeWithTag("Level 1 stars", true).onChildren().assertAll(
                hasContentDescription("Star")
            )

            onNodeWithText("LEVEL 1").performTouchInput { swipeUp() }
            val level2Stars = onNodeWithTag("Level 2 stars", true).onChildren()
            level2Stars[0].assertContentDescriptionEquals("Star")
            level2Stars[1].assertContentDescriptionEquals("Star")
            level2Stars[2].assertContentDescriptionEquals("Empty Star")

            onNodeWithText("LEVEL 2").performTouchInput { swipeUp() }
            val level3Stars = onNodeWithTag("Level 3 stars", true).onChildren()
            level3Stars[0].assertContentDescriptionEquals("Star")
            level3Stars[1].assertContentDescriptionEquals("Empty Star")
            level3Stars[2].assertContentDescriptionEquals("Empty Star")

            onNodeWithText("LEVEL 3").performTouchInput { swipeUp() }
            onNodeWithTag("Level 4 stars", true).onChildren().assertAll(
                hasContentDescription("Empty Star")
            )
        }
    }

}