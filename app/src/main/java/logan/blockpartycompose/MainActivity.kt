package logan.blockpartycompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import dagger.hilt.android.AndroidEntryPoint
import logan.blockpartycompose.navigation.Navigation
import logan.blockpartycompose.ui.screens.levelsMenu.LevelsViewModel
import logan.blockpartycompose.ui.theme.BlockPartyComposeTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    private val myViewModel: LevelsViewModel by viewModels()

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BlockPartyComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Navigation()
                }
            }
        }
    }
}

