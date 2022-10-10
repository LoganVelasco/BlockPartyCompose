package logan.blockpartycompose.feature.level

import androidx.compose.ui.semantics.SemanticsProperties.TestTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import logan.blockpartycompose.MainActivity
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import logan.blockpartycompose.utils.*

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class LevelsScreenFeatureTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        clearProgress(composeTestRule.activity)
    }

    @Test
    fun displaysInitialLevel() {
        composeTestRule.apply {
            openLevel(1, LevelSet.EASY)

            onNodeWithTag("level").onChildren()[9].assert(hasTestTag("player"))
            onNodeWithTag("level").onChildren()[18].assert(hasTestTag("goal"))
            onNodeWithTag("level").onChildren().filter(hasTestTag("empty")).assertCountEquals(22)
        }
    }

    @Test
    fun displaysSuccessScreenWithStars_whenLevelCompleted() {
        composeTestRule.apply {
            openLevel(1, LevelSet.EASY)

            movePlayerRight()
            movePlayerDown()
            movePlayerDown()

            waitUntilExists(hasText("You Did it!"), 1000)
            onNodeWithText("Level 1 Completed in 3 moves!").assertIsDisplayed()

            var stars = onNodeWithTag("stars", true).onChildren()
            stars[0].assertContentDescriptionEquals("Star")
            stars[1].assertContentDescriptionEquals("Star")
            stars[2].assertContentDescriptionEquals("Star")

            onNodeWithText("Try Again").performClick()

            movePlayerLeft()
            movePlayerRight()
            movePlayerRight()
            movePlayerDown()
            movePlayerDown()

            waitUntilExists(hasText("You Did it!"), 1000)
            onNodeWithText("Level 1 Completed in 5 moves!").assertIsDisplayed()

            stars = onNodeWithTag("stars", true).onChildren()
            stars[0].assertContentDescriptionEquals("Star")
            stars[1].assertContentDescriptionEquals("Star")
            stars[2].assertContentDescriptionEquals("Empty Star")

            onNodeWithText("Try Again").performClick()

            movePlayerLeft()
            movePlayerRight()
            movePlayerLeft()
            movePlayerRight()
            movePlayerRight()
            movePlayerDown()
            movePlayerDown()

            waitUntilExists(hasText("You Did it!"), 1000)
            onNodeWithText("Level 1 Completed in 7 moves!").assertIsDisplayed()

            stars = onNodeWithTag("stars", true).onChildren()
            stars[0].assertContentDescriptionEquals("Star")
            stars[1].assertContentDescriptionEquals("Empty Star")
            stars[2].assertContentDescriptionEquals("Empty Star")
        }
    }

    @Test
    fun clickingNextLevel_goesToNextLevel() {
        composeTestRule.apply {
            openLevel(1, LevelSet.EASY)

            movePlayerRight()
            movePlayerDown()
            movePlayerDown()

            waitUntilExists(hasText("You Did it!"), 1000)
            onNodeWithText("Next Level").performClick()

            onNodeWithTag("level").onChildren()[13].assert(hasTestTag("player"))
            onNodeWithTag("level").onChildren()[0].assert(hasTestTag("enemy"))
            onNodeWithTag("level").onChildren()[19].assert(hasTestTag("goal"))
            onNodeWithTag("level").onChildren().filter(hasTestTag("empty")).assertCountEquals(21)

        }
    }

    @Test
    fun clickingNextLevelOnLastLevel_goesToLevelsMenu() {
        composeTestRule.apply {
            openLevel(10, LevelSet.EASY)

            movePlayerRight()
            movePlayerRight()
            movePlayerRight()
            movePlayerRight()
            movePlayerRight()
            movePlayerLeft()
            movePlayerLeft()
            movePlayerLeft()
            movePlayerLeft()
            movePlayerLeft()
            movePlayerUp()
            movePlayerUp()
            movePlayerUp()
            movePlayerUp()
            movePlayerUp()
            movePlayerUp()
            movePlayerRight()
            movePlayerUp()
            movePlayerRight()
            movePlayerRight()
            movePlayerRight()

            waitUntilExists(hasText("You Did it!"), 1000)
            onNodeWithText("Next Level").performClick()

            onNodeWithText("3/30 Stars Collected").assertIsDisplayed()
            onNodeWithText("3/90 Total").assertIsDisplayed()
        }
    }

    @Test
    fun clickingRestartOnFailureScreen_restartsLevel() {
        composeTestRule.apply {
            openLevel(2, LevelSet.EASY)

            movePlayerUp()
            movePlayerUp()

            waitUntilExists(hasText("You Died!"), 1000)

            onNodeWithText("Try Again").performClick()

            onNodeWithTag("level").onChildren()[13].assert(hasTestTag("player"))
            onNodeWithTag("level").onChildren()[0].assert(hasTestTag("enemy"))
            onNodeWithTag("level").onChildren()[19].assert(hasTestTag("goal"))
            onNodeWithTag("level").onChildren().filter(hasTestTag("empty")).assertCountEquals(21)

        }
    }

    @Test
    fun enemyMovesAfterPlayer() {
        composeTestRule.apply {
            openLevel(3, LevelSet.EASY)

            movePlayerRight()

            onNodeWithTag("level").onChildren()[10].assert(hasTestTag("player"))
            onNodeWithTag("level").onChildren()[2].assert(hasTestTag("enemy"))
            waitUntil { onNodeWithTag("level").onChildren()[6].fetchSemanticsNode().config[TestTag] == "enemy" }
            movePlayerRight()
            waitUntilExists(hasText("You Died!"), 1000)
            onNodeWithText("Try Again").performClick()

            movePlayerLeft()

            onNodeWithTag("level").onChildren()[8].assert(hasTestTag("player"))
            onNodeWithTag("level").onChildren()[0].assert(hasTestTag("enemy"))
            waitUntil { onNodeWithTag("level").onChildren()[4].fetchSemanticsNode().config[TestTag] == "enemy" }
            movePlayerDown()
            waitUntilExists(hasText("You Died!"), 1000)
            onNodeWithText("Try Again").performClick()

            movePlayerDown()

            onNodeWithTag("level").onChildren()[13].assert(hasTestTag("player"))
            onNodeWithTag("level").onChildren()[1].assert(hasTestTag("enemy"))
        }
    }

}