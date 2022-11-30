package logan.blockpartycompose.ui.screens.welcomeScreen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import logan.blockpartycompose.R

@Composable
fun WelcomeScreen(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .clickable { navController.navigate("playMenu") }
    ) {

        val infiniteTransition = rememberInfiniteTransition()
        val alpha by infiniteTransition.animateFloat(
            initialValue = 24f,
            targetValue = 30f,
            animationSpec = infiniteRepeatable(
                animation = tween(delayMillis = 200, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        Text(
            text = "Welcome to\nBlock Party",
            textAlign = TextAlign.Center,
            fontSize = 36.sp,
            modifier = Modifier
                .padding(50.dp)
        )

        Text(
            text = stringResource(R.string.tap_anywhere_to_start),
            textAlign = TextAlign.Center,
            fontSize = alpha.sp,
            modifier = Modifier
                .padding(start = 50.dp, end = 50.dp, bottom = 100.dp)
        )
    }
}