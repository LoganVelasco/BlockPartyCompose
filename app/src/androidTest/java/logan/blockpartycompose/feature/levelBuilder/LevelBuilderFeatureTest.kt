package logan.blockpartycompose.feature.levelBuilder

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import logan.blockpartycompose.MainActivity
import logan.blockpartycompose.utils.*
import logan.blockpartycompose.R

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

            onNodeWithTag(activity.getString(R.string.level)).onChildren().assertAll(hasTestTag(activity.getString(R.string.empty)))
        }
    }

    @Test
    fun blockPaletteAddsCorrectBlock() {
        composeTestRule.apply {
            openLevelBuilder()

            onNodeWithTag(activity.getString(R.string.player)).performClick()
            onNodeWithTag(activity.getString(R.string.selected_block, activity.getString(R.string.player))).assertIsDisplayed()
            onNodeWithTag(activity.getString(R.string.player)).assertDoesNotExist()
            blockAtIndex(0).performClick()
            blockAtIndex(0).assert(hasTestTag(activity.getString(R.string.player)))

            onNodeWithTag(activity.getString(R.string.enemy)).performClick()
            onNodeWithTag(activity.getString(R.string.selected_block, activity.getString(R.string.enemy))).assertIsDisplayed()
            onNodeWithTag(activity.getString(R.string.enemy)).assertDoesNotExist()
            blockAtIndex(1).performClick()
            blockAtIndex(1).assert(hasTestTag(activity.getString(R.string.enemy)))

            onNodeWithTag(activity.getString(R.string.goal)).performClick()
            onNodeWithTag(activity.getString(R.string.selected_block, activity.getString(R.string.goal))).assertIsDisplayed()
            onNodeWithTag(activity.getString(R.string.goal)).assertDoesNotExist()
            blockAtIndex(2).performClick()
            blockAtIndex(2).assert(hasTestTag(activity.getString(R.string.goal)))

            onNodeWithTag(activity.getString(R.string.movable)).performClick()
            onNodeWithTag(activity.getString(R.string.selected_block, activity.getString(R.string.movable))).assertIsDisplayed()
            onNodeWithTag(activity.getString(R.string.movable)).assertDoesNotExist()
            blockAtIndex(3).performClick()
            blockAtIndex(3).assert(hasTestTag(activity.getString(R.string.movable)))

            onNodeWithTag(activity.getString(R.string.unmovable)).performClick()
            onNodeWithTag(activity.getString(R.string.selected_block, activity.getString(R.string.unmovable))).assertIsDisplayed()
            onNodeWithTag(activity.getString(R.string.unmovable)).assertDoesNotExist()
            blockAtIndex(4).performClick()
            blockAtIndex(4).assert(hasTestTag(activity.getString(R.string.unmovable)))

            onNodeWithTag(activity.getString(R.string.palette)).onChildren().filterToOne(hasTestTag(activity.getString(R.string.empty))).performClick()
            onNodeWithTag(activity.getString(R.string.selected_block, activity.getString(R.string.empty))).assertIsDisplayed()
            blockAtIndex(5).performClick()
            blockAtIndex(5).assert(hasTestTag(activity.getString(R.string.empty)))

        }
    }

    @Test
    fun backingOutOnEmptyLevelShowsNoPopup() {
        composeTestRule.apply {
            openLevelBuilder()
            onNodeWithTag(activity.getString(R.string.back_button)).performClick()
            onNodeWithText(activity.getString(R.string.save_level_question)).assertDoesNotExist()
        }
    }

    @Test
    fun backingOutOnInProgressShowsPopup() {
        composeTestRule.apply {
            openLevelBuilder()

            onNodeWithTag(activity.getString(R.string.player)).performClick()
            blockAtIndex(0).performClick()
            onNodeWithTag(activity.getString(R.string.goal)).performClick()
            blockAtIndex(9).performClick()

            onNodeWithTag(activity.getString(R.string.back_button)).performClick()
            waitUntilExists(hasText(activity.getString(R.string.save_level_question)))

            onNodeWithText(activity.getString(R.string.save)).performClick()
            onNodeWithText(activity.getString(R.string.save_level)).assertIsDisplayed()

        }
    }

    @Test
    fun savingNewLevel() {
        composeTestRule.apply {
            openLevelBuilder()

            onNodeWithTag(activity.getString(R.string.player)).performClick()
            blockAtIndex(0).performClick()
            onNodeWithTag(activity.getString(R.string.goal)).performClick()
            blockAtIndex(9).performClick()

            onNodeWithContentDescription(activity.getString(R.string.save)).performClick()
            waitUntilExists(hasText(activity.getString(R.string.save_level)))
            onNodeWithTag(activity.getString(R.string.level_name)).performTextInput("CustomLevel")

            onNodeWithText(activity.getString(R.string.save)).performClick()

            //TODO add to test after snackbar added

        }
    }


    @Test
    fun backingOutOnInProgressAfterSavingShowsNoPopup() {
        composeTestRule.apply {
            openLevelBuilder()

            onNodeWithTag(activity.getString(R.string.player)).performClick()
            blockAtIndex(0).performClick()
            onNodeWithTag(activity.getString(R.string.goal)).performClick()
            blockAtIndex(9).performClick()

            onNodeWithTag(activity.getString(R.string.back_button)).performClick()
            onNodeWithText(activity.getString(R.string.save_level)).assertDoesNotExist()
        }
    }

    @Test
    fun clearAllClearsLevel() {
        composeTestRule.apply {
            openLevelBuilder()

            onNodeWithTag(activity.getString(R.string.player)).performClick()
            blockAtIndex(0).performClick()
            onNodeWithTag(activity.getString(R.string.enemy)).performClick()
            blockAtIndex(5).performClick()
            onNodeWithTag(activity.getString(R.string.goal)).performClick()
            blockAtIndex(9).performClick()

            onNodeWithText(activity.getString(R.string.clear_all)).performClick()

            onNodeWithTag(activity.getString(R.string.level)).onChildren().assertAll(hasTestTag(activity.getString(R.string.empty)))
        }
    }

}