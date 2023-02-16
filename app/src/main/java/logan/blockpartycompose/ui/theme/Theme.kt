package logan.blockpartycompose.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import logan.blockpartycompose.ui.components.DarkColorsDefault
import logan.blockpartycompose.ui.components.DarkColorsGreen
import logan.blockpartycompose.ui.components.DarkColorsGrey
import logan.blockpartycompose.ui.components.DarkColorsPurple
import logan.blockpartycompose.ui.components.LightColorsDefault
import logan.blockpartycompose.ui.components.LightColorsGreen
import logan.blockpartycompose.ui.components.LightColorsGrey
import logan.blockpartycompose.ui.components.LightColorsPurple


@Composable
fun BlockPartyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colors: Int = 0,
    content: @Composable () -> Unit
) {
    val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val context = LocalContext.current

    val colorScheme =
        if (colors == -1 && dynamicColor) {
            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        } else if (colors == 1) {
            if (darkTheme) {
                DarkColorsGreen
            } else {
                LightColorsGreen
            }
        } else if (colors == 2) {
            if (darkTheme) {
                DarkColorsPurple
            } else {
                LightColorsPurple
            }
        }else if (colors == 3) {
            if (darkTheme) {
                DarkColorsGrey
            } else {
                LightColorsGrey
            }
        } else {
            if (darkTheme) {
                DarkColorsDefault
            } else {
                LightColorsDefault
            }
        }


    val systemUiController = rememberSystemUiController()

    systemUiController.setSystemBarsColor(
        color = colorScheme.surfaceColorAtElevation(1.dp)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = BlockPartyTypography,
        content = content
    )
}