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
fun RedBox(onClick: () -> Unit) {
    Block(modifier = Modifier
        .border(1.dp, Color.Black)
        .background(Color.Red)
        .clickable { onClick() }
    )
}

@Composable
fun BlueBox(onClick: () -> Unit) {
    Block(modifier = Modifier
        .border(1.dp, Color.Black)
        .background(Color.Blue)
        .clickable { onClick() }
    )
}

@Composable
fun YellowBox(onClick: () -> Unit) {
    Block(modifier = Modifier
        .border(1.dp, Color.Black)
        .background(Color.Yellow)
        .clickable { onClick() }
    )
}

@Composable
fun GrayBox(onClick: () -> Unit) {
    Block(modifier = Modifier
        .border(1.dp, Color.DarkGray)
        .background(Color.Gray)
        .clickable { onClick() }
    )
}

@Composable
fun BlackBox(onClick: () -> Unit) {
    Block(modifier = Modifier
        .border(1.dp, Color.DarkGray)
        .background(Color.Black)
        .clickable { onClick() }
    )
}

@Composable
fun GreenBox(onClick: () -> Unit) {
    Block(modifier = Modifier
        .border(1.dp, Color.Black)
        .background(Color.Green)
        .clickable { onClick() }
    )
}