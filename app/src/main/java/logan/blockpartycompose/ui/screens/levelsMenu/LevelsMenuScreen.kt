package logan.blockpartycompose.ui.screens.levelsMenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import logan.blockpartycompose.ui.screens.level.BackIcon

@Composable
fun LevelsMenu(
    navController: NavController,
    levelSet: LevelSet,
){
    val viewModel:LevelsViewModel = hiltViewModel()
    val levels = viewModel.getLevels(levelSet)

    Column {
        BackIcon(backClicked = { navController.navigateUp() }, modifier = Modifier.padding(10.dp))
        LazyColumn(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding()
        ){
            item {
                Text(text = levelSet.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            items(levels.size) { index ->
                val level = levels[index]
                Button(
                    onClick = {
                                navController.navigate("level/${levelSet.name}/${level.name}")
                              },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(25.dp)
                ) {
                    Text(text = level.name)
                }
            }
        }
    }
}

enum class LevelSet(string: String){
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard"),
    CUSTOM("Custom")
}
