package logan.blockpartycompose.ui.screens.levelsMenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import logan.blockpartycompose.data.models.Level
import logan.blockpartycompose.ui.screens.level.BackIcon

@Composable
fun LevelsMenu(
    onNavigate: (String) -> Unit,
    navigateUp: () -> Unit,
    levelSet: LevelSet,
    viewModel: LevelsMenuViewModel = hiltViewModel()
) {
    val levels by viewModel.levels.observeAsState()

    if (levels == null) {
        viewModel.getLevels(levelSet)
    } else {
        LevelsMenuScreen(
            onNavigate = onNavigate,
            navigateUp = navigateUp,
            levelSet = levelSet,
            levels = levels!!
        )
    }
}

@Composable
fun LevelsMenuScreen(
    onNavigate: (String) -> Unit,
    navigateUp: () -> Unit,
    levelSet: LevelSet,
    levels: List<Level>
) {
    Column {
        BackIcon(onClick = navigateUp, modifier = Modifier.padding(10.dp))
        LazyColumn(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding()
        ) {
            item {
                Text(text = levelSet.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            items(levels.size) { index ->
                val levelName = levels[index]
                val levelRoute = "level/${levelSet.name}/${levelName.name}"
                Button(
                    onClick = {
                        onNavigate(levelRoute)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(25.dp)
                ) {
                    Text(text = levelName.name)
                }
            }
        }
    }
}

enum class LevelSet(string: String) {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard"),
    CUSTOM("Custom")
}
