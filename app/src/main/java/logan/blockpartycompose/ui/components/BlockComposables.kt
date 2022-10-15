package logan.blockpartycompose.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
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
            .fillMaxWidth()
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

    val selectedModifier = if (isSelected) {
        Modifier
            .border(4.dp, selectedColor)
            .testTag("selected:$testTag")
    } else {
        Modifier
            .border(1.dp, defaultColor)
            .testTag(testTag)
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
    size: Dp = 50.dp,
) {
    Block(
        onClick = onClick,
        isSelected = isSelected,
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
    size: Dp = 50.dp,
) {
    Block(
        onClick = onClick,
        isSelected = isSelected,
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
    size: Dp = 50.dp,
) {
    Block(
        onClick = onClick,
        isSelected = isSelected,
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
    size: Dp = 50.dp,
) {
    Block(
        onClick = onClick,
        isSelected = isSelected,
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
    size: Dp = 50.dp,
) {
    Block(
        onClick = onClick,
        isSelected = isSelected,
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
    size: Dp = 50.dp,
) {
    Block(
        onClick = onClick,
        isSelected = isSelected,
        size = size,
        modifier = modifier,
        backgroundColor = Color.Green,
        defaultColor = Color.Black,
        selectedColor = Color.White,
        testTag = stringResource(R.string.movable)
    )
}