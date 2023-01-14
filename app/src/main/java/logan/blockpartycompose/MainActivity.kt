package logan.blockpartycompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import dagger.hilt.android.AndroidEntryPoint
import logan.blockpartycompose.navigation.Navigation
import logan.blockpartycompose.ui.theme.BlockPartyTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity  @Inject constructor(): ComponentActivity() {

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BlockPartyTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colorScheme.background) {
                    Navigation()
                }
            }
        }
    }
}

