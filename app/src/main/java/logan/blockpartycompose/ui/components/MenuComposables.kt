package logan.blockpartycompose.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import logan.blockpartycompose.R

@Composable
fun BaseHeader(
    modifier: Modifier = Modifier,
    startIcon: ImageVector? = null,
    endIcon: ImageVector? = null,
    withBorder: Boolean = true,
    middleContent: @Composable () -> Unit = {},
    startIconOnclick: () -> Unit = { },
    endIconOnclick: () -> Unit = { }
) {
    var newMod = modifier
    if (withBorder)newMod = modifier.then(Modifier.border(2.dp, MaterialTheme.colorScheme.outline, RectangleShape))// TODO: Make dynamic
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = newMod.then(
            Modifier
                .fillMaxHeight(.07f)
                .fillMaxWidth()
        )
    ) {
        if (startIcon != null) {
            IconButton(
                onClick = startIconOnclick,
            ) {
                Icon(
                    startIcon, tint = MaterialTheme.colorScheme.onSurfaceVariant, contentDescription = "", modifier = Modifier
                        .scale(1.5f)
                        .padding(10.dp)
                )
            }
        }else {
            Spacer(modifier = Modifier.width(endIcon?.defaultWidth?.times(1.5f) ?: 0.dp))
        }

        middleContent()

        if (endIcon != null) {
            IconButton(
                onClick = endIconOnclick,
            ) {

                Icon(
                    endIcon, tint = MaterialTheme.colorScheme.onSurfaceVariant, contentDescription = "", modifier = Modifier
                        .scale(1.5f)
                        .padding(10.dp)
                )
            }

        }else{
            Spacer(modifier = Modifier.width(startIcon?.defaultWidth?.times(1.5f) ?: 0.dp))
        }
    }
}

@Composable
fun BackIcon(backClicked: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(
        onClick = backClicked,
        modifier = modifier
    ) {
        Icon(Icons.Filled.ArrowBack, tint = MaterialTheme.colorScheme.onSurfaceVariant, contentDescription = stringResource(R.string.back_to_menu))
    }
}