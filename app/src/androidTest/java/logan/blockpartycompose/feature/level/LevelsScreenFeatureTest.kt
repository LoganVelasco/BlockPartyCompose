package logan.blockpartycompose.feature.level

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.semantics.SemanticsProperties.TestTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import logan.blockpartycompose.MainActivity
import logan.blockpartycompose.ui.screens.levelsMenu.LevelSet
import logan.blockpartycompose.utils.*
import logan.blockpartycompose.R

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

    fun hasText(@StringRes resId: Int) =
        hasText(composeTestRule.activity.getString(resId))
    fun <A: ComponentActivity> hasText(@StringRes resId: Int, activity: A) =
        hasTestTag(activity.getString(resId))
    @Test
    fun displaysInitialLevel() {

        composeTestRule.apply {
            openLevel(1, LevelSet.EASY)

            blockAtIndex(9).assert(hasTestTag(activity.getString(R.string.player)))
            blockAtIndex(18).assert(hasTestTag(activity.getString(R.string.goal)))
            onNodeWithTag(activity.getString(R.string.level)).onChildren().filter(hasTestTag(activity.getString(R.string.empty))).assertCountEquals(22)
        }
    }

    @Test
    fun displaysSuccessScreenWithStars_whenLevelCompleted() {
        composeTestRule.apply {
            openLevel(1, LevelSet.EASY)

            movePlayerRight()
            movePlayerDown()
            movePlayerDown()

            waitUntilExists(hasText(activity.getString(R.string.you_did_it)))
            onNodeWithText(activity.getString(R.string.level_completed_in, "Level 1", 3)).assertIsDisplayed()

            assertStarsShown(3)

            onNodeWithText(activity.getString(R.string.try_again)).performClick()

            movePlayerLeft()
            movePlayerRight()
            movePlayerRight()
            movePlayerDown()
            movePlayerDown()

            waitUntilExists(hasText(activity.getString(R.string.you_did_it)))
            onNodeWithText(activity.getString(R.string.level_completed_in, "Level 1", 5)).assertIsDisplayed()

            assertStarsShown(2)

            onNodeWithText(activity.getString(R.string.try_again)).performClick()

            movePlayerLeft()
            movePlayerRight()
            movePlayerLeft()
            movePlayerRight()
            movePlayerRight()
            movePlayerDown()
            movePlayerDown()

            waitUntilExists(hasText(activity.getString(R.string.you_did_it)))
            onNodeWithText(activity.getString(R.string.level_completed_in, "Level 1", 7)).assertIsDisplayed()

            assertStarsShown(1)
        }
    }

    @Test // Flaky?
    fun clickingNextLevel_goesToNextLevel() {
        composeTestRule.apply {
            openLevel(1, LevelSet.EASY)

            movePlayerRight()
            movePlayerDown()
            movePlayerDown()

            waitUntilExists(hasText(activity.getString(R.string.you_did_it)))
            onNodeWithText(activity.getString(R.string.next_level)).performClick()

            blockAtIndex(13).assert(hasTestTag(activity.getString(R.string.player)))
            blockAtIndex(0).assert(hasTestTag(activity.getString(R.string.enemy)))
            blockAtIndex(19).assert(hasTestTag(activity.getString(R.string.goal)))
            onNodeWithTag(activity.getString(R.string.level)).onChildren().filter(hasTestTag(activity.getString(R.string.empty))).assertCountEquals(21)

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

            waitUntilExists(hasText(activity.getString(R.string.you_did_it)))
            onNodeWithText(activity.getString(R.string.next_level)).performClick()

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

            blockAtIndex(13).assert(hasTestTag("player"))
            blockAtIndex(0).assert(hasTestTag("enemy"))
            blockAtIndex(19).assert(hasTestTag("goal"))
            onNodeWithTag(activity.getString(R.string.level)).onChildren().filter(hasTestTag("empty")).assertCountEquals(21)

        }
    }

    @Test
    fun enemyMovesAfterPlayer() {
        composeTestRule.apply {
            openLevel(3, LevelSet.EASY)

            movePlayerRight()

            blockAtIndex(10).assert(hasTestTag("player"))
            blockAtIndex(2).assert(hasTestTag("enemy"))
            waitUntil { blockAtIndex(6).fetchSemanticsNode().config[TestTag] == "enemy" }
            movePlayerRight()
            waitUntilExists(hasText("You Died!"))
            onNodeWithText("Try Again").performClick()

            movePlayerLeft()

            blockAtIndex(8).assert(hasTestTag("player"))
            blockAtIndex(0).assert(hasTestTag("enemy"))
            waitUntil { blockAtIndex(4).fetchSemanticsNode().config[TestTag] == "enemy" }
            movePlayerDown()
            waitUntilExists(hasText("You Died!"))
            onNodeWithText("Try Again").performClick()

            movePlayerDown()

            blockAtIndex(13).assert(hasTestTag("player"))
            blockAtIndex(1).assert(hasTestTag("enemy"))
        }
    }

}