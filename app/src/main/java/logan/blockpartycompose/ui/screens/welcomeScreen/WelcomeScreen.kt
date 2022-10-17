package logan.blockpartycompose.ui.screens.welcomeScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
            fontSize = 25.sp,
            modifier = Modifier
                .padding(start = 50.dp, end = 50.dp, bottom = 100.dp)
        )
    }
}