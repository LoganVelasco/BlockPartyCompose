package logan.blockpartycompose.ui.screens.settings

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import logan.blockpartycompose.R
import logan.blockpartycompose.ui.theme.theme_1_dark_primary
import logan.blockpartycompose.ui.theme.theme_1_light_primary
import logan.blockpartycompose.ui.theme.theme_2_dark_primary
import logan.blockpartycompose.ui.theme.theme_2_light_primary
import logan.blockpartycompose.ui.theme.theme_3_dark_primary
import logan.blockpartycompose.ui.theme.theme_3_light_primary

@Composable
fun SettingsScreen(navController: NavController) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val theme = viewModel.state
    val restartApp by viewModel.restartApp.observeAsState()

    if (restartApp == true) restartApp(navController)
    val updateColorsOnClick = viewModel::updateColorScheme
    Settings(navController, theme, updateColorsOnClick)
}

@Composable
fun Settings(
    navController: NavController,
    currentTheme: Int,
    updateColorsOnClick: (colorScheme: Int) -> Unit
) {
    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = stringResource(R.string.change_theme),
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        ColorPicker(currentTheme = currentTheme, updateColorsOnClick)
    }

}

@Composable
fun ColorPicker(currentTheme: Int, updateColorsOnClick: (colorScheme: Int) -> Unit) {
    val modifier = Modifier
        .size(50.dp)
        .padding(5.dp)
        .clip(CircleShape)

    val defaultThemeColor = if (isSystemInDarkTheme()) {
        theme_1_dark_primary
    } else {
        theme_1_light_primary
    }
    val greenThemeColor = if (isSystemInDarkTheme()) {
        theme_2_dark_primary
    } else {
        theme_2_light_primary
    }
    val purpleThemeColor = if (isSystemInDarkTheme()) {
        theme_3_dark_primary
    } else {
        theme_3_light_primary
    }
    val dynamicColorEnabled = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (dynamicColorEnabled) {
            val materialYouColor = if (isSystemInDarkTheme()) {
                dynamicDarkColorScheme(LocalContext.current).primary
            } else {
                dynamicLightColorScheme(LocalContext.current).primary
            }
            if (currentTheme == -1) {
                Box(
                    modifier = modifier
                        .background(materialYouColor)
                        .border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                )
            } else {
                Box(
                    modifier = modifier
                        .background(materialYouColor)
                        .clickable { updateColorsOnClick(-1) }
                )
            }
        }
        if (currentTheme == 0) {
            Box(
                modifier = modifier
                    .background(defaultThemeColor)
                    .border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
            )
        } else {
            Box(
                modifier = modifier
                    .background(defaultThemeColor)
                    .clickable { updateColorsOnClick(0) }
            )
        }
        if (currentTheme == 1) {
            Box(
                modifier = modifier
                    .background(greenThemeColor)
                    .border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
            )
        } else {
            Box(
                modifier = modifier
                    .background(greenThemeColor)
                    .clickable { updateColorsOnClick(1) }
            )
        }
        if (currentTheme == 2) {
            Box(
                modifier = modifier
                    .background(purpleThemeColor)
                    .border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
            )
        } else {
            Box(
                modifier = modifier
                    .background(purpleThemeColor)
                    .clickable { updateColorsOnClick(2) }
            )
        }
    }
}


fun restartApp(navController: NavController) {
    navController.navigate("updatedSettings")
}