package logan.blockpartycompose.ui.screens.splash

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import logan.blockpartycompose.R
import logan.blockpartycompose.ui.theme.MainFont
import logan.blockpartycompose.ui.theme.enemy_color
import logan.blockpartycompose.ui.theme.enemy_color_outline
import logan.blockpartycompose.ui.theme.goal_color
import logan.blockpartycompose.ui.theme.goal_color_outline
import logan.blockpartycompose.ui.theme.movable_color
import logan.blockpartycompose.ui.theme.movable_color_outline
import logan.blockpartycompose.ui.theme.player_color
import logan.blockpartycompose.ui.theme.player_color_outline

@Composable
fun WelcomeScreen(navController: NavController, closeApp: () -> Unit) {
    val viewModel: SplashViewModel = hiltViewModel()
    val isTutorialMode = viewModel.isTutorialMode()

    BackHandler {
        closeApp()
    }

    val playerColor = remember { Animatable(player_color) }
    val enemyColor = remember { Animatable(enemy_color) }
    val movableColor = remember { Animatable(movable_color) }
    val goalColor = remember { Animatable(goal_color) }
    val playerOutlineColor = remember { Animatable(player_color_outline) }
    val enemyOutlineColor = remember { Animatable(enemy_color_outline) }
    val movableOutlineColor = remember { Animatable(movable_color_outline) }
    val goalOutlineColor = remember { Animatable(goal_color_outline) }
    val animSpec = tween<Color>(1500)
    LaunchedEffect(key1 = true) {
        repeat(10) {
            delay(1000)
            launch { playerColor.animateTo(movable_color, animationSpec = animSpec) }
            launch { playerOutlineColor.animateTo(movable_color_outline, animationSpec = animSpec) }
            launch { enemyColor.animateTo(player_color, animationSpec = animSpec) }
            launch { enemyOutlineColor.animateTo(player_color_outline, animationSpec = animSpec) }
            launch { movableColor.animateTo(goal_color, animationSpec = animSpec) }
            launch { movableOutlineColor.animateTo(goal_color_outline, animationSpec = animSpec) }
            launch { goalColor.animateTo(enemy_color, animationSpec = animSpec) }
            launch { goalOutlineColor.animateTo(enemy_color_outline, animationSpec = animSpec) }
            delay(2500)
            launch { playerColor.animateTo(goal_color, animationSpec = animSpec) }
            launch { playerOutlineColor.animateTo(goal_color_outline, animationSpec = animSpec) }
            launch { enemyColor.animateTo(movable_color, animationSpec = animSpec) }
            launch { enemyOutlineColor.animateTo(movable_color_outline, animationSpec = animSpec) }
            launch { movableColor.animateTo(enemy_color, animationSpec = animSpec) }
            launch { movableOutlineColor.animateTo(enemy_color_outline, animationSpec = animSpec) }
            launch { goalColor.animateTo(player_color, animationSpec = animSpec) }
            launch { goalOutlineColor.animateTo(player_color_outline, animationSpec = animSpec) }
            delay(2500)
            launch { playerColor.animateTo(enemy_color, animationSpec = animSpec) }
            launch { playerOutlineColor.animateTo(enemy_color_outline, animationSpec = animSpec) }
            launch { enemyColor.animateTo(goal_color, animationSpec = animSpec) }
            launch { enemyOutlineColor.animateTo(goal_color_outline, animationSpec = animSpec) }
            launch { movableColor.animateTo(player_color, animationSpec = animSpec) }
            launch { movableOutlineColor.animateTo(player_color_outline, animationSpec = animSpec) }
            launch { goalColor.animateTo(movable_color, animationSpec = animSpec) }
            launch { goalOutlineColor.animateTo(movable_color_outline, animationSpec = animSpec) }
            delay(2500)
            launch { playerColor.animateTo(player_color, animationSpec = animSpec) }
            launch { playerOutlineColor.animateTo(player_color_outline, animationSpec = animSpec) }
            launch { enemyColor.animateTo(enemy_color, animationSpec = animSpec) }
            launch { enemyOutlineColor.animateTo(enemy_color_outline, animationSpec = animSpec) }
            launch { movableColor.animateTo(movable_color, animationSpec = animSpec) }
            launch { movableOutlineColor.animateTo(movable_color_outline, animationSpec = animSpec) }
            launch { goalColor.animateTo(goal_color, animationSpec = animSpec) }
            launch { goalOutlineColor.animateTo(goal_color_outline, animationSpec = animSpec) }
            delay(1500)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .clickable {
                    if (isTutorialMode)
                        navController.navigate("tutorialMode")
                    else
                        navController.navigate("playMenu")
                }
        ) {

            val infiniteTransition = rememberInfiniteTransition()
            val alpha by infiniteTransition.animateFloat(
                initialValue = 18f,
                targetValue = 24f,
                animationSpec = infiniteRepeatable(
                    animation = tween(delayMillis = 200, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )



            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp)
                    .padding(top = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.block_party),
                    textAlign = TextAlign.Center,
                    fontSize = 46.sp,
                    fontFamily = MainFont,
                    lineHeight = 45.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .fillMaxWidth()
                        .padding(10.dp)
                )
            }

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {

                drawRoundRect(
                    color = goalColor.value,
                    size = Size(width = 100.dp.toPx(), height = 100.dp.toPx()),
                    cornerRadius = CornerRadius(10f, 10f),
                    topLeft = Offset(x = center.x, y = center.y),
                )
                drawRoundRect(
                    color = goalOutlineColor.value,
                    size = Size(width = 100.dp.toPx(), height = 100.dp.toPx()),
                    cornerRadius = CornerRadius(10f, 10f),
                    topLeft = Offset(x = center.x, y = center.y),
                    style = Stroke(4.dp.toPx())
                )

                drawRoundRect(
                    color = enemyColor.value,
                    size = Size(width = 100.dp.toPx(), height = 100.dp.toPx()),
                    cornerRadius = CornerRadius(10f, 10f),
                    topLeft = Offset(x = center.x, y = center.y - 105.dp.toPx()),
                )
                drawRoundRect(
                    color = enemyOutlineColor.value,
                    size = Size(width = 100.dp.toPx(), height = 100.dp.toPx()),
                    cornerRadius = CornerRadius(10f, 10f),
                    topLeft = Offset(x = center.x, y = center.y - 105.dp.toPx()),
                    style = Stroke(4.dp.toPx())
                )

                drawRoundRect(
                    color = playerColor.value,
                    size = Size(width = 100.dp.toPx(), height = 100.dp.toPx()),
                    cornerRadius = CornerRadius(10f, 10f),
                    topLeft = Offset(x = center.x - 105.dp.toPx(), y = center.y - 105.dp.toPx()),
                )
                drawRoundRect(
                    color = playerOutlineColor.value,
                    size = Size(width = 100.dp.toPx(), height = 100.dp.toPx()),
                    cornerRadius = CornerRadius(10f, 10f),
                    topLeft = Offset(x = center.x - 105.dp.toPx(), y = center.y - 105.dp.toPx()),
                    style = Stroke(4.dp.toPx())
                )

                drawRoundRect(
                    color = movableColor.value,
                    size = Size(width = 100.dp.toPx(), height = 100.dp.toPx()),
                    cornerRadius = CornerRadius(10f, 10f),
                    topLeft = Offset(x = center.x - 105.dp.toPx(), y = center.y),
                )
                drawRoundRect(
                    color = movableOutlineColor.value,
                    size = Size(width = 100.dp.toPx(), height = 100.dp.toPx()),
                    cornerRadius = CornerRadius(10f, 10f),
                    topLeft = Offset(x = center.x - 105.dp.toPx(), y = center.y),
                    style = Stroke(4.dp.toPx())
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(start = 15.dp, end = 15.dp, top = 25.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier

                ) {
                    Text(
                        text = stringResource(R.string.tap_anywhere_to_start),
                        textAlign = TextAlign.Center,
                        fontFamily = MainFont,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = alpha.sp,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(start = 7.dp, bottom = 10.dp, end = 7.dp, top = 10.dp)
                    )
                }
            }
        }
    }
}