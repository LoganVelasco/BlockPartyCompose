package logan.blockpartycompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import dagger.hilt.android.AndroidEntryPoint
import logan.blockpartycompose.data.DataRepository
import logan.blockpartycompose.navigation.Navigation
import logan.blockpartycompose.ui.screens.level.LevelsViewModel
import logan.blockpartycompose.ui.theme.BlockPartyComposeTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity  @Inject constructor(): ComponentActivity() {

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        dataRepository.easyFile = assets.open("Easy.txt")
//        dataRepository.mediumFile = assets.open("Medium.json")
//        dataRepository.hardFile = assets.open("Easy.json")
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

