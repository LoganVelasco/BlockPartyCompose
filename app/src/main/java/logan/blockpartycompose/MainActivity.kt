package logan.blockpartycompose

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import logan.blockpartycompose.navigation.Navigation
import logan.blockpartycompose.ui.theme.BlockPartyTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor() : ComponentActivity() {

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs: SharedPreferences =
            this.getSharedPreferences(getString(R.string.colors), Context.MODE_PRIVATE)

        val colors = prefs.getInt(getString(R.string.colors), 0)

        val restartApp = {
            val mIntent = intent
            mIntent.putExtra(getString(R.string.updated), true)
            finish()
            startActivity(mIntent)
        }

        val isUpdated = intent.getBooleanExtra(getString(R.string.updated), false)

        installSplashScreen()

        setContent {
            BlockPartyTheme(colors = colors) {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colorScheme.background) {
                    Navigation(
                        isUpdated = isUpdated,
                        resetApp = restartApp,
                        closeApp = { finish() })
                }
            }
        }
    }
}

