package logan.blockpartycompose.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager


@Composable
fun BaseTutorial(description: String, modifier: Modifier = Modifier,  content: @Composable() () -> Unit) {
    val configuration = LocalConfiguration.current
    Card(
        border = BorderStroke(5.dp, Color.DarkGray),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .width(configuration.screenWidthDp.dp)
            .padding(10.dp)
            .height(200.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(10.dp)
        ) {
            content()
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                text = description
            )

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
    BaseTutorial("This is the goal block.\nMove the Player Block here\n to complete the level.", modifier) {
        GoalBlock()
    }
}

@Composable
fun EnemyInfo(modifier: Modifier = Modifier) {
    BaseTutorial("This is the Enemy block. It moves twice after ever player move. It chases you always moving to the same row before moving up or down. Don't let it catch you or its game over!", modifier) {
        EnemyBlock()
    }
}

@Composable
fun UnmovableInfo(modifier: Modifier = Modifier) {
    BaseTutorial("This an obstacle.\nIt cannot be moved and blocks\nboth the player and enemy.", modifier) {
        UnmovableBlock()
    }
}

@Composable
fun MovableInfo(modifier: Modifier = Modifier) {
    BaseTutorial("This is the Movable block.\nIt can be pushed only by the player.\nPush 2 Movable blocks together\nand they both disappear!", modifier) {
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
        when(it) {
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