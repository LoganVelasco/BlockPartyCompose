package logan.blockpartycompose.ui.screens.mainMenu

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import logan.blockpartycompose.ui.components.DifficultyButton
import logan.blockpartycompose.ui.components.HeaderButton

@Composable
fun MainMenu(onNavigate: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        MainMenuHeader(onNavigate)
        MainMenuDifficulties(onNavigate)
    }
}

@Composable
fun MainMenuHeader(onNavigate: (String) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        HeaderButton(
            onNavigate = { onNavigate("levelBuilder") },
            label = "Level Builder",
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(10.dp))
        HeaderButton(
            onNavigate = { onNavigate("levels/custom") },
            label = "My Levels",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun MainMenuDifficulties(onNavigate: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(start = 20.dp, end = 20.dp)
    ) {
        DifficultyButton(onNavigate = { onNavigate("easy") }, label = "Easy")
        DifficultyButton(onNavigate = { onNavigate("medium") }, label = "Medium")
        DifficultyButton(onNavigate = { onNavigate("hard") }, label = "Hard", enabled = false)
    }
}