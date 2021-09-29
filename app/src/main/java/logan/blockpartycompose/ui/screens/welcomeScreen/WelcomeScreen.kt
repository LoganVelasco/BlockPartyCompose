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
import androidx.navigation.NavController


@Composable
fun WelcomeScreen(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
//        Image(bitmap = , contentDescription = "Welcome to Block Party")
        Text(
            text = "Welcome to\nBlock Party",
            textAlign = TextAlign.Center,
            fontSize = 36.sp,
            modifier = Modifier
                .padding(50.dp)
        )
        Button(
            onClick = { navController.navigate("playMenu") },
            modifier = Modifier
                .padding(start = 75.dp, end = 75.dp, top = 100.dp)
        ) {
            Text(text = "Play")
        }
    }
}
