package logan.blockpartycompose.ui.screens.welcomeScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import logan.blockpartycompose.ui.components.PlayButton


@Composable
fun WelcomeScreen(onNavigate: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        WelcomeTitle(text = "Welcome to\nBlock Party")
        PlayButton({ onNavigate("playMenu") })
    }
}

@Composable
fun WelcomeTitle(text: String, modifier: Modifier = Modifier){
    Text(
        text = text,
        textAlign = TextAlign.Center,
        fontSize = 36.sp,
        modifier = modifier
            .padding(50.dp)
    )
}