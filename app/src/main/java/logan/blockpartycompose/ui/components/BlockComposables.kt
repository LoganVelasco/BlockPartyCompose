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
import androidx.compose.ui.unit.dp


@Composable
fun Block(modifier: Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
    ) {
        Box(
            modifier = modifier
                .height(50.dp)
                .width(50.dp)
                .padding(5.dp)
                .clip(RectangleShape)
        )
    }
}

@Composable
fun RedBox(
    onClick: () -> Unit,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (isSelected) {
        Block(
            modifier = modifier
                .border(4.dp, Color.White)
                .background(Color.Red)
                .clickable { onClick() }
                .testTag("enemy"))
    } else
        Block(modifier = modifier
            .border(1.dp, Color.Black)
            .background(Color.Red)
            .clickable { onClick() }
            .testTag("enemy")
        )
}

@Composable
fun BlueBox(
    onClick: () -> Unit,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (isSelected) {
        Block(
            modifier = modifier
                .border(4.dp, Color.White)
                .background(Color.Blue)
                .clickable { onClick() }
                .testTag("player"))
    } else
        Block(modifier = modifier
            .border(1.dp, Color.Black)
            .background(Color.Blue)
            .clickable { onClick() }
            .testTag("player")
        )
}

@Composable
fun YellowBox(
    onClick: () -> Unit,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (isSelected) {
        Block(
            modifier = modifier
                .border(4.dp, Color.White)
                .background(Color.Yellow)
                .clickable { onClick() }
                .testTag("goal"))
    } else
        Block(modifier = modifier
            .border(1.dp, Color.Black)
            .background(Color.Yellow)
            .clickable { onClick() }
            .testTag("goal")
        )
}

@Composable
fun GrayBox(
    onClick: () -> Unit,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (isSelected) {
        Block(
            modifier = modifier
                .border(4.dp, Color.White)
                .background(Color.Gray)
                .clickable { onClick() }
            .testTag("empty"))
    } else
        Block(modifier = modifier
            .border(1.dp, Color.DarkGray)
            .background(Color.Gray)
            .clickable { onClick() }
            .testTag("empty")
        )
}

@Composable
fun BlackBox(
    onClick: () -> Unit,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (isSelected) {
        Block(
            modifier = modifier
                .border(4.dp, Color.White)
                .background(Color.Black)
                .clickable { onClick() }
                .testTag("x"))
    } else
        Block(modifier = modifier
            .border(1.dp, Color.DarkGray)
            .background(Color.Black)
            .clickable { onClick() }
            .testTag("x")
        )
}

@Composable
fun GreenBox(
    onClick: () -> Unit,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (isSelected) {
        Block(
            modifier = modifier
                .border(4.dp, Color.White)
                .background(Color.Green)
                .clickable { onClick() }
                .testTag("movable"))
    } else
        Block(modifier = modifier
            .border(1.dp, Color.Black)
            .background(Color.Green)
            .clickable { onClick() }
            .testTag("movable")
        )
}