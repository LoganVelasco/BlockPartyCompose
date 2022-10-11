package logan.blockpartycompose.feature.levelBuilder

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import logan.blockpartycompose.MainActivity
import logan.blockpartycompose.utils.*

import org.junit.runner.RunWith

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@RunWith(AndroidJUnit4::class)
class LevelBuilderFeatureTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {

    }

    @Test
    fun initialLevelBuilderScreenShowsEmptyLevel() {
        composeTestRule.apply {
            openLevelBuilder()

            onNodeWithTag("level").onChildren().assertAll(hasTestTag("empty"))
        }
    }

    @Test
    fun blockPaletteAddsCorrectBlock() {
        composeTestRule.apply {
            openLevelBuilder()

            onNodeWithTag("player").performClick()
            onNodeWithTag("selected:player").assertIsDisplayed()
            onNodeWithTag("player").assertDoesNotExist()
            onNodeWithTag("level").onChildren()[0].performClick()
            onNodeWithTag("level").onChildren()[0].assert(hasTestTag("player"))

            onNodeWithTag("enemy").performClick()
            onNodeWithTag("selected:enemy").assertIsDisplayed()
            onNodeWithTag("enemy").assertDoesNotExist()
            onNodeWithTag("level").onChildren()[1].performClick()
            onNodeWithTag("level").onChildren()[1].assert(hasTestTag("enemy"))

            onNodeWithTag("goal").performClick()
            onNodeWithTag("selected:goal").assertIsDisplayed()
            onNodeWithTag("goal").assertDoesNotExist()
            onNodeWithTag("level").onChildren()[2].performClick()
            onNodeWithTag("level").onChildren()[2].assert(hasTestTag("goal"))

            onNodeWithTag("movable").performClick()
            onNodeWithTag("selected:movable").assertIsDisplayed()
            onNodeWithTag("movable").assertDoesNotExist()
            onNodeWithTag("level").onChildren()[3].performClick()
            onNodeWithTag("level").onChildren()[3].assert(hasTestTag("movable"))

            onNodeWithTag("x").performClick()
            onNodeWithTag("selected:x").assertIsDisplayed()
            onNodeWithTag("x").assertDoesNotExist()
            onNodeWithTag("level").onChildren()[4].performClick()
            onNodeWithTag("level").onChildren()[4].assert(hasTestTag("x"))

            onNodeWithTag("palette").onChildren().filterToOne(hasTestTag("empty")).performClick()
            onNodeWithTag("selected:empty").assertIsDisplayed()
            onNodeWithTag("level").onChildren()[5].performClick()
            onNodeWithTag("level").onChildren()[5].assert(hasTestTag("empty"))

        }
    }

    @Test
    fun backingOutOnEmptyLevelShowsNoPopup() {
        composeTestRule.apply {
            openLevelBuilder()
            onNodeWithTag("back button").performClick()
            onNodeWithText("Save Level?").assertDoesNotExist()
        }
    }

    @Test
    fun backingOutOnInProgressShowsPopup() {
        composeTestRule.apply {
            openLevelBuilder()

            onNodeWithTag("player").performClick()
            onNodeWithTag("level").onChildren()[0].performClick()
            onNodeWithTag("goal").performClick()
            onNodeWithTag("level").onChildren()[9].performClick()

            onNodeWithTag("back button").performClick()
            waitUntilExists(hasText("Save Level?"))

            onNodeWithText("Save").performClick()
            onNodeWithText("Save Level").assertIsDisplayed()

        }
    }

    @Test
    fun savingNewLevel() {
        composeTestRule.apply {
            openLevelBuilder()

            onNodeWithTag("player").performClick()
            onNodeWithTag("level").onChildren()[0].performClick()
            onNodeWithTag("goal").performClick()
            onNodeWithTag("level").onChildren()[9].performClick()

            onNodeWithContentDescription("Save").performClick()
            waitUntilExists(hasText("Save Level"))
            onNodeWithTag("level name").performTextInput("CustomLevel")

            onNodeWithText("Save").performClick()

            //TODO add to test after snackbar added

        }
    }


    @Test
    fun backingOutOnInProgressAfterSavingShowsNoPopup() {
        composeTestRule.apply {
            openLevelBuilder()

            onNodeWithTag("player").performClick()
            onNodeWithTag("level").onChildren()[0].performClick()
            onNodeWithTag("goal").performClick()
            onNodeWithTag("level").onChildren()[9].performClick()

            onNodeWithTag("back button").performClick()
            onNodeWithText("Save Level").assertDoesNotExist()
        }
    }

    @Test
    fun clearAllClearsLevel() {
        composeTestRule.apply {
            openLevelBuilder()

            onNodeWithTag("player").performClick()
            onNodeWithTag("level").onChildren()[0].performClick()
            onNodeWithTag("enemy").performClick()
            onNodeWithTag("level").onChildren()[5].performClick()
            onNodeWithTag("goal").performClick()
            onNodeWithTag("level").onChildren()[9].performClick()

            onNodeWithText("Clear All").performClick()

            onNodeWithTag("level").onChildren().assertAll(hasTestTag("empty"))
        }
    }

}