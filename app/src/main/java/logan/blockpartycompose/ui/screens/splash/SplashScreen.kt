package logan.blockpartycompose.ui.screens.splash

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import logan.blockpartycompose.R

@Composable
fun WelcomeScreen(navController: NavController, closeApp: () -> Unit) {
    val viewModel: SplashViewModel = hiltViewModel()
    val isTutorialMode = viewModel.isTutorialMode()

    BackHandler {
        closeApp()
    }

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
            initialValue = 24f,
            targetValue = 30f,
            animationSpec = infiniteRepeatable(
                animation = tween(delayMillis = 200, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp)) {
            Text(
                text = "Welcome to\nBlock Party",
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                lineHeight = 45.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxWidth()
                    .padding(10.dp)
            )
        }
        Card(
            modifier = Modifier
            .padding(start = 25.dp, bottom = 100.dp, end = 25.dp, top = 25.dp)) {
            Text(
                text = stringResource(R.string.tap_anywhere_to_start),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = alpha.sp,
                modifier = Modifier.background(MaterialTheme.colorScheme.primary)
                    .padding(15.dp)
            )
        }
    }

}