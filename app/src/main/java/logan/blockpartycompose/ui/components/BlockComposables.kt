package logan.blockpartycompose.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import logan.blockpartycompose.R


@Composable
fun BaseBlock(size: Dp = 50.dp, modifier: Modifier) {
    Column(
        modifier = Modifier
            .width(IntrinsicSize.Min)
            .wrapContentSize(Alignment.Center)
    ) {
        Box(
            modifier = modifier
                .height(size)
                .width(size)
                .padding(5.dp)
                .clip(RectangleShape)
        )
    }
}

@Composable
fun Block(
    backgroundColor: Color,
    defaultColor: Color,
    selectedColor: Color,
    testTag: String,
    modifier: Modifier = Modifier,
    size: Dp = 50.dp,
    isSelected: Boolean = false,
    isPulsing: Boolean = false,
    onClick: (() -> Unit)?,
) {
    val defaultModifier = if (onClick != null) {
        Modifier
            .background(backgroundColor)
            .clickable { onClick() }
    } else {
        Modifier
            .background(backgroundColor)
    }

    var selectedModifier = if (isSelected) {
        Modifier
            .border(4.dp, selectedColor)
            .testTag(stringResource(id = R.string.selected_block, testTag))
    } else {
        Modifier
            .border(1.dp, defaultColor)
            .testTag(testTag)
    }

    if(isPulsing){
        val infiniteTransition = rememberInfiniteTransition()
        val targetColor = when(backgroundColor){
            Color.Yellow -> Color.Black
            else -> Color.White
        }
        val color by infiniteTransition.animateColor(
            initialValue = defaultColor,
            targetValue = targetColor,
            animationSpec = infiniteRepeatable(
                animation = tween(delayMillis = 200, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        val borderSize by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 2f,
            animationSpec = infiniteRepeatable(
                animation = tween(delayMillis = 200, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        val pulseModifier = Modifier
            .border(borderSize.dp, color)
        selectedModifier = selectedModifier.then(pulseModifier)
    }


    BaseBlock(
        size = size,
        modifier = modifier.then(defaultModifier.then(selectedModifier))
    )
}

@Composable
fun EnemyBlock(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    isSelected: Boolean = false,
    isPulsing: Boolean = false,
    size: Dp = 50.dp,
) {
    Block(
        onClick = onClick,
        isSelected = isSelected,
        isPulsing = isPulsing,
        size = size,
        modifier = modifier,
        backgroundColor = Color.Red,
        defaultColor = Color.Black,
        selectedColor = Color.White,
        testTag = stringResource(R.string.enemy)
    )
}


@Composable
fun PlayerBlock(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    isSelected: Boolean = false,
    isPulsing: Boolean = false,
    size: Dp = 50.dp,
) {
    Block(
        onClick = onClick,
        isSelected = isSelected,
        isPulsing = isPulsing,
        size = size,
        modifier = modifier,
        backgroundColor = Color.Blue,
        defaultColor = Color.Black,
        selectedColor = Color.White,
        testTag = stringResource(R.string.player)
    )
}


@Composable
fun GoalBlock(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    isSelected: Boolean = false,
    isPulsing: Boolean = false,
    size: Dp = 50.dp,
) {
    Block(
        onClick = onClick,
        isSelected = isSelected,
        isPulsing = isPulsing,
        size = size,
        modifier = modifier,
        backgroundColor = Color.Yellow,
        defaultColor = Color.Black,
        selectedColor = Color.White,
        testTag = stringResource(R.string.goal)
    )
}

@Composable
fun EmptyBlock(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    isSelected: Boolean = false,
    isPulsing: Boolean = false,
    size: Dp = 50.dp,
) {
    Block(
        onClick = onClick,
        isSelected = isSelected,
        isPulsing = isPulsing,
        size = size,
        modifier = modifier,
        backgroundColor = Color.Gray,
        defaultColor = Color.DarkGray,
        selectedColor = Color.White,
        testTag = stringResource(R.string.empty)
    )
}

@Composable
fun UnmovableBlock(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    isSelected: Boolean = false,
    isPulsing: Boolean = false,
    size: Dp = 50.dp,
) {
    Block(
        onClick = onClick,
        isSelected = isSelected,
        isPulsing = isPulsing,
        size = size,
        modifier = modifier,
        backgroundColor = Color.Black,
        defaultColor = Color.DarkGray,
        selectedColor = Color.White,
        testTag = stringResource(R.string.unmovable)
    )
}

@Composable
fun MovableBlock(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    isSelected: Boolean = false,
    isPulsing: Boolean = false,
    size: Dp = 50.dp,
) {
    Block(
        onClick = onClick,
        isSelected = isSelected,
        isPulsing = isPulsing,
        size = size,
        modifier = modifier,
        backgroundColor = Color.Green,
        defaultColor = Color.Black,
        selectedColor = Color.White,
        testTag = stringResource(R.string.movable)
    )
}