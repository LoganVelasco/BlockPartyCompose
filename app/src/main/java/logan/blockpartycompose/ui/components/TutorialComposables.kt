package logan.blockpartycompose.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import logan.blockpartycompose.R
import logan.blockpartycompose.ui.screens.level.PostLevelScreen
import logan.blockpartycompose.ui.screens.level.SuccessStars


@Composable
fun BaseTutorial(
    description: String,
    modifier: Modifier = Modifier,
    backOnClick: (() -> Unit)? = null,
    forwardOnClick: (() -> Unit)? = null,
    animateForward: Boolean = false,
    content: @Composable (() -> Unit)? = null
) {

    val configuration = LocalConfiguration.current
    OutlinedCard(
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .width(configuration.screenWidthDp.dp)
            .padding(10.dp)
            .height(200.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .fillMaxHeight()
        ) {
            if (forwardOnClick != null || backOnClick != null) Spacer(modifier = Modifier.height(25.dp))
            else Spacer(modifier = Modifier.height(1.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .height(IntrinsicSize.Min)
                    .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 10.dp)
            ) {
                if (content != null) {
                    Spacer(modifier = Modifier.width(20.dp))
                    content()
                    Spacer(modifier = Modifier.width(20.dp))
                }
                Text(
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    text = description,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            if (forwardOnClick != null || backOnClick != null) {
                var alpha = 1.5f
                if (animateForward) {
                    val infiniteTransition = rememberInfiniteTransition()
                    val animatedAlpha by infiniteTransition.animateFloat(
                        initialValue = 1.5f,
                        targetValue = 2f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(durationMillis = 500, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        )
                    )
                    alpha = animatedAlpha
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (backOnClick != null) {
                        IconButton(
                            onClick = backOnClick,
                        ) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "Undo",
                                modifier = Modifier
                                    .scale(1.5f)
                                    .padding(10.dp)
                            )
                        }
                    } else Spacer(modifier = Modifier.width(1.dp))

                    if (forwardOnClick != null) {
                        IconButton(
                            onClick = forwardOnClick,
                        ) {
                            Icon(
                                Icons.Filled.ArrowForward,
                                contentDescription = "Undo",
                                modifier = Modifier
                                    .scale(alpha)
                                    .padding(10.dp)
                            )
                        }
                    } else Spacer(modifier = Modifier.width(1.dp))
                }
            } else Spacer(modifier = Modifier.width(1.dp))
        }
    }
}

@Composable
fun TutorialWindow(
    tutorialStage: Int, tutorialProgress: Int,
    forwardOnClick: (() -> Unit)? = null,
) {
    when (tutorialStage) {
        0 -> {
            TutorialStageOne(tutorialProgress)
            return
        }

        1 -> {
            TutorialStageTwo(tutorialProgress, forwardOnClick)
        }

        2 -> {
            TutorialStageThree(tutorialProgress, forwardOnClick)
        }

        3 -> {
            TutorialStageFour(tutorialProgress)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TutorialStageFour(infoProgress: Int) {
    if (infoProgress == 3) return
    val scope = rememberCoroutineScope()
    val state = rememberPagerState()
    LaunchedEffect(key1 = infoProgress) {
        delay(175)
        state.animateScrollToPage(page = infoProgress)
    }
    HorizontalPager(
        count = 3, state = state, modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
    ) {
        when (it) {
            0 -> {
                BaseTutorial(
                    "This button will undo your last move",
                    forwardOnClick = { scope.launch { state.scrollToPage(1) } },
                    animateForward = true
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_undo_24),
                        contentDescription = "Undo",
                    )
                }
            }

            1 -> {
                BaseTutorial(
                    "This button will restart the level",
                    forwardOnClick = { scope.launch { state.scrollToPage(2) } },
                    backOnClick = { scope.launch { state.scrollToPage(0) } },
                    animateForward = true
                ) {
                    Icon(Icons.Filled.Refresh, contentDescription = "Restart")
                }
            }

            2 -> {
                BaseTutorial("This button will show how each block functions",
                    backOnClick = { scope.launch { state.scrollToPage(0) } }
                ) {
                    Icon(Icons.Filled.Info, contentDescription = "Info")
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TutorialPlayMenuWindow() {
    val state = rememberPagerState()
    val getShownHint = (0..4).random()
    LaunchedEffect(key1 = getShownHint) {
        state.scrollToPage(page = getShownHint)
    }
    HorizontalPager(
        state = state,
        count = 5, modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
    ) {
        when (it) {
            0 -> {
                BaseTutorial("Tap Level builder to build a custom level of your own!")
            }

            1 -> {
                BaseTutorial("Earn more stars to unlock harder difficulties")
            }

            2 -> {
                BaseTutorial("Tap the gear icon to apply a custom theme")
            }

            3 -> {
                BaseTutorial("More Levels coming soon!")
            }

            4 -> {
                BaseTutorial("Tired of hints? Disable this window in settings")
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TutorialStageOne(
    progress: Int = 0,
) {
    val state = rememberPagerState()
    LaunchedEffect(key1 = progress) {
        delay(200)
        state.animateScrollToPage(page = progress)
    }
    HorizontalPager(
        count = progress + 1, state = state, modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
    ) {
        when (it) {
            0 -> {
                BaseTutorial("Move the Blue block to the gold block to beat the level") {
                    PlayerBlock()
                }
            }

            1 -> {
                BaseTutorial("Tap the gold block to beat the level") {
                    GoalBlock()
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TutorialStageTwo(progress: Int, forwardOnClick: (() -> Unit)?) {
    val state = rememberPagerState()
    LaunchedEffect(state) {
        // Collect from the pager state a snapshotFlow reading the currentPage
        snapshotFlow { state.currentPage }.collect { page ->
            if (page == 2 && progress == 0 && forwardOnClick != null) {
                forwardOnClick()
            }
        }
    }
    val scope = rememberCoroutineScope()
    HorizontalPager(
        count = 3, state = state, modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
    ) {
        when (it) {
            0 -> {
                BaseTutorial(
                    "This is the Enemy block. It moves twice after ever player move.",
                    forwardOnClick = { scope.launch { state.scrollToPage(1) } },
                    animateForward = true
                ) {
                    EnemyBlock()
                }
            }

            1 -> {
                BaseTutorial(
                    "It can only move closer to the Blue block and always tries to move horizontally first if possible.",
                    forwardOnClick = {
                        scope.launch { state.scrollToPage(2) }
                    },
                    backOnClick = { scope.launch { state.scrollToPage(0) } },
                    animateForward = true
                )
            }

            2 -> {
                BaseTutorial(
                    "Don't let it catch you or its game over!",
                    backOnClick = { scope.launch { state.scrollToPage(1) } }
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TutorialStageThree(progress: Int, forwardOnClick: (() -> Unit)?) {
    val state = rememberPagerState()
    LaunchedEffect(state) {
        // Collect from the pager state a snapshotFlow reading the currentPage
        snapshotFlow { state.currentPage }.collect { page ->
            if (page == 1 && progress == 0 && forwardOnClick != null) {
                forwardOnClick()
            }
        }
    }
    val scope = rememberCoroutineScope()
    HorizontalPager(
        state = state,
        count = 2, modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
    ) {
        when (it) {
            0 -> {
                BaseTutorial(
                    "This an obstacle. It cannot be moved and blocks both the player and enemy.",
                    forwardOnClick = { scope.launch { state.scrollToPage(1) } },
                    animateForward = true
                ) {
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
        count = 2, modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        when (it) {
            0 -> {
                BaseTutorial(description = "Complete levels in as few moves as possible to earn all 3 stars")
            }

            1 -> {
                BaseTutorial(description = "Earning stars will let you unlock harder levels")
            }

        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SecondWinTutorialWindow() {
    HorizontalPager(
        count = 2, modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        when (it) {
            0 -> {
                BaseTutorial(description = "Earning stars will let you unlock harder levels")
            }

            1 -> {
                BaseTutorial(description = "Complete levels in as few moves as possible to earn all 3 stars")
            }

        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun FirstLossTutorialWindow() {
    HorizontalPager(
        count = 1, modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        when (it) {
            0 -> {
                BaseTutorial(description = "Don't let the red block catch you!")
            }
        }
    }
}

@Composable
fun TutorialSuccessScreen(
    nextLevelOnClick: () -> Unit, movesUsed: Int, tutorialState: Int
) {
    if (movesUsed == 0) return
    PostLevelScreen {
        Text(text = stringResource(id = R.string.you_did_it), fontSize = 36.sp)
        Text(
            text = stringResource(id = R.string.tutorial_level_completed_in, movesUsed),
            fontSize = 26.sp
        )
        SuccessStars(3)
        when (tutorialState) {
            0 -> {
                FirstWinTutorialWindow()
            }

            1 -> {
                SecondWinTutorialWindow()
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()
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
    PostLevelScreen {
        Text(text = stringResource(id = R.string.you_died), fontSize = 36.sp)
        FirstLossTutorialWindow()
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { tryAgainOnClick() }) {
                Text(text = stringResource(R.string.try_again))
            }
        }
    }
}

@Composable
fun PlayerInfo(
    modifier: Modifier = Modifier,
    backOnClick: (() -> Unit)? = null,
    forwardOnClick: (() -> Unit)? = null,
) {
    BaseTutorial(
        "Tap a surrounding square to move",
        forwardOnClick = forwardOnClick,
        backOnClick = backOnClick,
        modifier = modifier
    ) {
        PlayerBlock()
    }
}

@Composable
fun GoalInfo(
    modifier: Modifier = Modifier,
    backOnClick: (() -> Unit)? = null,
    forwardOnClick: (() -> Unit)? = null,
) {
    BaseTutorial(
        "This is the goal block.\nMove the Player Block here\n to complete the level.",
        forwardOnClick = forwardOnClick,
        backOnClick = backOnClick,
        modifier = modifier
    ) {
        GoalBlock()
    }
}

@Composable
fun EnemyInfo1(
    modifier: Modifier = Modifier,
    backOnClick: (() -> Unit)? = null,
    forwardOnClick: (() -> Unit)? = null,
) {
    BaseTutorial(
        "This is the Enemy block. It moves twice after ever player move.",
        forwardOnClick = forwardOnClick,
        backOnClick = backOnClick,
        modifier = modifier
    ) {
        EnemyBlock()
    }
}

@Composable
fun EnemyInfo2(
    modifier: Modifier = Modifier,
    backOnClick: (() -> Unit)? = null,
    forwardOnClick: (() -> Unit)? = null,
) {
    BaseTutorial(
        "It can only move closer to you and tries to move horizontally first if possible.",
        forwardOnClick = forwardOnClick,
        backOnClick = backOnClick,
        modifier = modifier
    ) {
        EnemyBlock()
    }

}

@Composable
fun EnemyInfo3(
    modifier: Modifier = Modifier,
    backOnClick: (() -> Unit)? = null,
    forwardOnClick: (() -> Unit)? = null,
) {
    BaseTutorial(
        "Don't let it catch you or it's game over!",
        forwardOnClick = forwardOnClick,
        backOnClick = backOnClick,
        modifier = modifier
    ) {
        EnemyBlock()
    }

}

@Composable
fun UnmovableInfo(
    modifier: Modifier = Modifier,
    backOnClick: (() -> Unit)? = null,
    forwardOnClick: (() -> Unit)? = null,
) {
    BaseTutorial(
        "This an obstacle. It cannot be moved and blocks both the player and enemy.",
        forwardOnClick = forwardOnClick,
        backOnClick = backOnClick,
        modifier = modifier
    ) {
        UnmovableBlock()
    }
}

@Composable
fun MovableInfo1(
    modifier: Modifier = Modifier,
    backOnClick: (() -> Unit)? = null,
    forwardOnClick: (() -> Unit)? = null,
) {
    BaseTutorial(
        "This is the Movable block.\nIt can be pushed only by the player.",
        forwardOnClick = forwardOnClick,
        backOnClick = backOnClick,
        modifier = modifier
    ) {
        MovableBlock()
    }
}

@Composable
fun MovableInfo2(
    modifier: Modifier = Modifier,
    backOnClick: (() -> Unit)? = null,
    forwardOnClick: (() -> Unit)? = null,
) {
    BaseTutorial(
        "Push two Movable blocks together and they both disappear!",
        forwardOnClick = forwardOnClick,
        backOnClick = backOnClick,
        modifier = modifier
    ) {
        MovableBlock()
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HelpCard(
    count: Int,
    modifier: Modifier = Modifier,
    currentCard: Int = 0
) {
    val scope = rememberCoroutineScope()
    val state = rememberPagerState()

    LaunchedEffect(key1 = currentCard) {
        if (currentCard > 0) state.scrollToPage(page = currentCard)
    }

    HorizontalPager(
        count,
        state = state,
        modifier = modifier
            .fillMaxWidth()
            .height(175.dp)
    ) {
        var forwardOnClick: (() -> Unit)? = { scope.launch { state.scrollToPage(it + 1) } }
        val backOnClick: (() -> Unit) = { scope.launch { state.scrollToPage(it - 1) } }
        when (it) {
            0 -> PlayerInfo(forwardOnClick = forwardOnClick)
            1 -> GoalInfo(forwardOnClick = forwardOnClick, backOnClick = backOnClick)
            2 -> EnemyInfo1(forwardOnClick = forwardOnClick, backOnClick = backOnClick)
            3 -> EnemyInfo2(forwardOnClick = forwardOnClick, backOnClick = backOnClick)
            4 -> EnemyInfo3(forwardOnClick = forwardOnClick, backOnClick = backOnClick)
            5 -> {
                if (count <= 6) forwardOnClick = null
                UnmovableInfo(forwardOnClick = forwardOnClick, backOnClick = backOnClick)
            }

            6 -> MovableInfo1(backOnClick = backOnClick, forwardOnClick = forwardOnClick)
            7 -> MovableInfo2(backOnClick = backOnClick)
        }
    }
}


@Preview
@Composable
fun Preview() {
    HelpCard(6)
}