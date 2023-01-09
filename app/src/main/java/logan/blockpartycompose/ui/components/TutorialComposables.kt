package logan.blockpartycompose.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import logan.blockpartycompose.R
import logan.blockpartycompose.ui.screens.level.PostLevelScreen
import logan.blockpartycompose.ui.screens.level.SuccessStars
import kotlin.reflect.KFunction1


@Composable
fun BaseTutorial(
    description: String,
    modifier: Modifier = Modifier,
    backOnClick: (() -> Unit)? = null,
    forwardOnClick: (() -> Unit)? = null,
    content: @Composable() (() -> Unit)? = null
) {
    val configuration = LocalConfiguration.current
    Card(
        border = BorderStroke(5.dp, Color.DarkGray),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .width(configuration.screenWidthDp.dp)
            .padding(10.dp)
            .height(200.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
        ) {
//            if (forwardOnClick != null || backOnClick != null) Spacer(modifier = Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(10.dp)
            ) {
                if (content != null) {
                    Spacer(modifier = Modifier.width(20.dp))
                    content()
                    Spacer(modifier = Modifier.width(20.dp))
                }
                Text(
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    text = description
                )
            }
//            if (forwardOnClick != null || backOnClick != null) {
//                Row(
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    if (backOnClick != null) {
//                        IconButton(
//                            onClick = backOnClick,
//                        ) {
//                            Icon(
//                                Icons.Filled.ArrowBack,
//                                contentDescription = "Undo",
//                                modifier = Modifier
//                                    .scale(1.5f)
//                                    .padding(10.dp)
//                            )
//                        }
//                    } else Spacer(modifier = Modifier.width(1.dp))
//
//                    if (forwardOnClick != null) {
//                        IconButton(
//                            onClick = forwardOnClick,
//                        ) {
//                            Icon(
//                                Icons.Filled.ArrowForward,
//                                contentDescription = "Undo",
//                                modifier = Modifier
//                                    .scale(1.5f)
//                                    .padding(10.dp)
//                            )
//                        }
//                    } else Spacer(modifier = Modifier.width(1.dp))
//                }
//            }
//            else Spacer(modifier = Modifier.width(2.dp))
        }
    }
}

@Composable
fun TutorialWindow(tutorialStage: Int, infoProgress: Int,backOnClick: (() -> Unit)? = null,
                   forwardOnClick: (() -> Unit)? = null,) {
    when (tutorialStage) {
        0 -> {
            TutorialStageOne(infoProgress, backOnClick, forwardOnClick)
            return
        }

        1 -> {
            TutorialStageTwo()
        }

        2 -> {
            TutorialStageThree(infoProgress)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TutorialPlayMenuWindow() {
    HorizontalPager(
        count = 2,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        when (it) {
            0 -> {
                BaseTutorial("Earn more stars to unlock harder difficulties")
            }

            1 -> {
                BaseTutorial("Tap Level builder to build a custom level of your own!")
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TutorialStageOne(
    progress: Int = 0,
    backOnClick: (() -> Unit)? = null,
    forwardOnClick: (() -> Unit)? = null,
) {
    val state = rememberPagerState()
    LaunchedEffect(key1 = progress) {
        delay(200)
        state.animateScrollToPage(page = progress)
    }
    HorizontalPager(
        count = progress + 1,
        state = state,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        when (it) {
            0 -> {
                BaseTutorial("Move the Blue block to the gold block to beat the level") {
                    PlayerBlock()
                }
            }

            1 -> {
                BaseTutorial("Tap a surround block to move closer to the Gold Block") {
                    PlayerBlock()
                }
            }

            2 -> {
                BaseTutorial("Tap the gold block to beat the level") {
                    GoalBlock()
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TutorialStageTwo() {
    HorizontalPager(
        count = 1,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        when (it) {
            0 -> {
                BaseTutorial("This is the Enemy block. It moves twice after ever player move. It chases you always moving to the same row before moving up or down. Don't let it catch you or its game over!") {
                    EnemyBlock()
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TutorialStageThree(infoProgress: Int) {
    HorizontalPager(
        count = 2,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        when (it) {
            0 -> {
                BaseTutorial("This an obstacle. It cannot be moved and blocks both the player and enemy.") {
                    UnmovableBlock()
                }
            }

            1 -> {
                BaseTutorial("Use this block to trap the enemy and make it safely to the goal") {
                    UnmovableBlock()
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun FirstWinTutorialWindow() {
    HorizontalPager(
        count = 2,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        when (it) {
            0 -> {
                BaseTutorial(description = "Complete levels in as few moves as possible to earn all 3 stars") {
                    FilledStar()
                }
            }

            1 -> {
                BaseTutorial(description = "Earning stars will let you unlock harder levels") {
                    FilledStar()
                }
            }

        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun FirstLossTutorialWindow() {
    HorizontalPager(
        count = 2,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        when (it) {
            0 -> {
                BaseTutorial(description = "You Suck!")
            }
            1 -> {
                BaseTutorial(description = "Get Good")
            }
        }
    }
}

@Composable
fun TutorialSuccessScreen(
    nextLevelOnClick: () -> Unit,
    movesUsed: Int,
    tutorialState: Int
) {
    if (movesUsed == 0) return
    PostLevelScreen() {
        Text(text = stringResource(id = R.string.you_did_it))
        Text(text = stringResource(id = R.string.tutorial_level_completed_in, movesUsed))
        SuccessStars(3)
        FirstWinTutorialWindow()
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { nextLevelOnClick() }) {
                Text(text = stringResource(R.string.continue_tutorial))
            }
        }
    }
}

@Composable
fun TutorialFailureScreen(
    tryAgainOnClick: () -> Unit,
) {
    PostLevelScreen() {
        Text(text = stringResource(id = R.string.you_died))
        FirstLossTutorialWindow()
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { tryAgainOnClick() }) {
                Text(text = stringResource(R.string.try_again))
            }
        }
    }
}

@Composable
fun PlayerInfo(modifier: Modifier = Modifier) {
    BaseTutorial("Tap a surrounding square to move", modifier) {
        PlayerBlock()
    }
}

@Composable
fun GoalInfo(modifier: Modifier = Modifier) {
    BaseTutorial(
        "This is the goal block.\nMove the Player Block here\n to complete the level.",
        modifier
    ) {
        GoalBlock()
    }
}

@Composable
fun EnemyInfo(modifier: Modifier = Modifier) {
    BaseTutorial(
        "This is the Enemy block. It moves twice after ever player move. It chases you always moving to the same row before moving up or down. Don't let it catch you or its game over!",
        modifier
    ) {
        EnemyBlock()
    }
}

@Composable
fun UnmovableInfo(modifier: Modifier = Modifier) {
    BaseTutorial(
        "This an obstacle. It cannot be moved and blocks both the player and enemy.",
        modifier
    ) {
        UnmovableBlock()
    }
}

@Composable
fun MovableInfo(modifier: Modifier = Modifier) {
    BaseTutorial(
        "This is the Movable block.\nIt can be pushed only by the player.\nPush 2 Movable blocks together\nand they both disappear!",
        modifier
    ) {
        MovableBlock()
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HelpCard(count: Int, modifier: Modifier = Modifier) {
    HorizontalPager(
        count,
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        when (it) {
            0 -> PlayerInfo()
            1 -> GoalInfo()
            2 -> EnemyInfo()
            3 -> UnmovableInfo()
            4 -> MovableInfo()
        }
    }
}


@Preview
@Composable
fun Preview() {
    HelpCard(5)
}