package logan.blockpartycompose.ui.screens.playMenu

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun PlayMenu(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Button(
                onClick = { navController.navigate("levelBuilder") },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Level Builder",
                    fontSize = 12.sp
                )
            }
            Spacer(Modifier.width(10.dp))
            Button(
                onClick = { navController.navigate("levels/custom") },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "My Levels",
                    fontSize = 12.sp
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(start = 20.dp, end = 20.dp)
        ) {
            Button(
                onClick = { navController.navigate("easy") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Easy")
            }
            Button(onClick = { navController.navigate("levels/medium") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Medium")
            }
            Button(onClick = { navController.navigate("levels/hard") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Hard")
            }
        }
    }
}
