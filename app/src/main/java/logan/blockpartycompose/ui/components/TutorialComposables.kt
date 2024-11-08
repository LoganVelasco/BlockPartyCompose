package logan.blockpartycompose.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import logan.blockpartycompose.R
import logan.blockpartycompose.ui.screens.level.PostLevelScreen
import logan.blockpartycompose.ui.screens.level.SuccessStars
import logan.blockpartycompose.ui.theme.MainFont


@Composable
fun BaseTutorial(
    description: String,
    modifier: Modifier = Modifier,
    backOnClick: (() -> Unit)? = null,
    forwardOnClick: (() -> Unit)? = null,
    animateForward: Boolean = false,
    content: @Composable (() -> Unit)? = null
) {
    OutlinedCard(
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(200.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .fillMaxHeight()
        ) {
            if (backOnClick != null) {
                IconButton(
                    onClick = backOnClick,
                    modifier = Modifier
                        .align(Alignment.Bottom)
                        .padding(start = 5.dp)
                        .weight(.1f)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.show_previous_hint),
                        modifier = Modifier
                            .scale(1.5f)
                    )
                }
            } else Spacer(modifier = Modifier.weight(.1f))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .weight(.8f)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxHeight()
            ) {
                if (content != null) {
                    content()
                    Spacer(modifier = Modifier.width(7.dp))
                }
                Text(
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontFamily = MainFont,
                    text = description,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (forwardOnClick != null) {
                var alpha = 1.5f
                if (animateForward) {
                    val infiniteTransition = rememberInfiniteTransition(label = "")
                    val animatedAlpha by infiniteTransition.animateFloat(
                        initialValue = 1.5f,
                        targetValue = 2f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(durationMillis = 500, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ), label = "AnimateForward"
                    )
                    alpha = animatedAlpha
                }
                IconButton(
                    onClick = forwardOnClick,
                    modifier = Modifier
                        .align(Alignment.Bottom)
                        .weight(.1f)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = stringResource(R.string.show_next_hint),
                        modifier = Modifier
                            .scale(alpha)
                            .padding(end = 5.dp)
                    )
                }
            } else Spacer(modifier = Modifier.weight(.1f))
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

@Composable
fun TutorialStageFour(infoProgress: Int) {
    if (infoProgress == 3) return
    val scope = rememberCoroutineScope()
    val state = androidx.compose.foundation.pager.rememberPagerState(initialPage = 0, pageCount = { 3 })
    LaunchedEffect(key1 = infoProgress) {
        delay(175)
        state.animateScrollToPage(page = infoProgress)
    }
    HorizontalPager(
        state = state, modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
    ) {
        when (it) {
            0 -> {
                BaseTutorial(
                    stringResource(R.string.undo_tut),
                    forwardOnClick = { scope.launch { state.scrollToPage(1) } },
                    animateForward = true
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_undo_24),
                        contentDescription = stringResource(R.string.undo),
                    )
                }
            }

            1 -> {
                BaseTutorial(
                    stringResource(R.string.restart_tut),
                    forwardOnClick = { scope.launch { state.scrollToPage(2) } },
                    backOnClick = { scope.launch { state.scrollToPage(0) } },
                    animateForward = true
                ) {
                    Icon(
                        Icons.Filled.Refresh,
                        contentDescription = stringResource(R.string.restart)
                    )
                }
            }

            2 -> {
                BaseTutorial(
                    stringResource(R.string.info_tut),
                    backOnClick = { scope.launch { state.scrollToPage(0) } }
                ) {
                    Icon(Icons.Filled.Info, contentDescription = stringResource(R.string.info))
                }
            }
        }
    }
}

@Composable
fun TutorialPlayMenuWindow() {
    val state = androidx.compose.foundation.pager.rememberPagerState(initialPage = 0, pageCount = { 5 })
    val getShownHint = (0..4).random()
    LaunchedEffect(key1 = getShownHint) {
        state.scrollToPage(page = getShownHint)
    }
    HorizontalPager(
        state = state,
        modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
    ) {
        when (it) {
            0 -> {
                BaseTutorial(stringResource(R.string.hint_1))
            }

            1 -> {
                BaseTutorial(stringResource(R.string.hint_2))
            }

            2 -> {
                BaseTutorial(stringResource(R.string.hint_3))
            }

            3 -> {
                BaseTutorial(stringResource(R.string.hint_4))
            }

            4 -> {
                BaseTutorial(stringResource(R.string.hint_5))
            }
        }
    }
}

@Composable
fun TutorialStageOne(
    progress: Int = 0,
) {
    val state = androidx.compose.foundation.pager.rememberPagerState(initialPage = 0, pageCount = { progress + 1})
    LaunchedEffect(key1 = progress) {
        delay(200)
        state.animateScrollToPage(page = progress)
    }
    HorizontalPager(
         state = state, modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
    ) {
        when (it) {
            0 -> {
                BaseTutorial(stringResource(R.string.initial_tut_1)) {
                    PlayerBlock()
                }
            }

            1 -> {
                BaseTutorial(stringResource(R.string.initial_tut_2)) {
                    GoalBlock()
                }
            }
        }
    }
}

@Composable
fun TutorialStageTwo(progress: Int, forwardOnClick: (() -> Unit)?) {
    val state = androidx.compose.foundation.pager.rememberPagerState(initialPage = 0, pageCount = { 3 })
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
        state = state,
        modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
    ) {
        when (it) {
            0 -> {
                BaseTutorial(
                    stringResource(R.string.enemy_tut_1),
                    forwardOnClick = { scope.launch { state.scrollToPage(1) } },
                    animateForward = true
                ) {
                    EnemyBlock()
                }
            }

            1 -> {
                BaseTutorial(
                    stringResource(R.string.enemy_tut_2),
                    forwardOnClick = {
                        scope.launch { state.scrollToPage(2) }
                    },
                    backOnClick = { scope.launch { state.scrollToPage(0) } },
                    animateForward = true
                )
            }

            2 -> {
                BaseTutorial(
                    stringResource(R.string.enemy_tut_3),
                    backOnClick = { scope.launch { state.scrollToPage(1) } }
                )
            }
        }
    }
}

@Composable
fun TutorialStageThree(progress: Int, forwardOnClick: (() -> Unit)?) {
    val state = androidx.compose.foundation.pager.rememberPagerState(initialPage = 0, pageCount = { 2 })
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
        modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
    ) {
        when (it) {
            0 -> {
                BaseTutorial(
                    stringResource(R.string.unmovable_tut),
                    forwardOnClick = { scope.launch { state.scrollToPage(1) } },
                    animateForward = true
                ) {
                    UnmovableBlock()
                }
            }

            1 -> {
                BaseTutorial(stringResource(R.string.unmovable_tut_2),
                    backOnClick = { scope.launch { state.scrollToPage(0) } }) {
                    UnmovableBlock()
                }
            }
        }
    }
}

@Composable
fun FirstWinTutorialWindow() {
    val state = androidx.compose.foundation.pager.rememberPagerState(initialPage = 0, pageCount = { 2 })

    HorizontalPager(
        state = state,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        when (it) {
            0 -> {
                BaseTutorial(description = stringResource(R.string.win_tut_2))
            }

            1 -> {
                BaseTutorial(description = stringResource(R.string.win_tut_1))
            }

        }
    }
}

@Composable
fun SecondWinTutorialWindow() {
    val state = androidx.compose.foundation.pager.rememberPagerState(initialPage = 0, pageCount = { 2 })

    HorizontalPager(
        state = state,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        when (it) {
            0 -> {
                BaseTutorial(description = stringResource(R.string.win_tut_1))
            }

            1 -> {
                BaseTutorial(description = stringResource(R.string.win_tut_2))
            }

        }
    }
}

@Composable
fun FirstLossTutorialWindow() {
    val state = androidx.compose.foundation.pager.rememberPagerState(initialPage = 0, pageCount = { 1 })

    HorizontalPager(
        state = state,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        when (it) {
            0 -> {
                BaseTutorial(description = stringResource(R.string.loss_tut))
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
        Text(text = stringResource(id = R.string.you_did_it), fontFamily = MainFont,
            fontSize = 36.sp)
        Text(
            text = stringResource(id = R.string.tutorial_level_completed_in, movesUsed), fontFamily = MainFont,
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
                Text(text = stringResource(R.string.continue_tutorial), fontFamily = MainFont)
            }
        }
    }
}

@Composable
fun TutorialFailureScreen(
    tryAgainOnClick: () -> Unit,
) {
    PostLevelScreen {
        Text(text = stringResource(id = R.string.you_died), fontFamily = MainFont,
            fontSize = 36.sp)
        FirstLossTutorialWindow()
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { tryAgainOnClick() }) {
                Text(text = stringResource(R.string.try_again), fontFamily = MainFont
                )
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
        stringResource(R.string.player_tut),
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
        stringResource(R.string.goal_tut),
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
        stringResource(R.string.enemy_tut_1),
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
        stringResource(R.string.enemy_tut_2),
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
        stringResource(R.string.enemy_tut_3),
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
        stringResource(R.string.unmovable_tut),
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
        stringResource(R.string.movable_tut_1),
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
        stringResource(R.string.movable_tut_2),
        forwardOnClick = forwardOnClick,
        backOnClick = backOnClick,
        modifier = modifier
    ) {
        MovableBlock()
    }
}

@Composable
fun HelpCard(
    count: Int,
    modifier: Modifier = Modifier,
    currentCard: Int = 0
) {
    val scope = rememberCoroutineScope()
    val state = androidx.compose.foundation.pager.rememberPagerState(initialPage = 0, pageCount = { count })

    LaunchedEffect(key1 = currentCard) {
        if (currentCard > 0) state.scrollToPage(page = currentCard)
    }

    HorizontalPager(
        state = state,
        modifier = modifier
            .animateContentSize()
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