package logan.blockpartycompose.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun BaseHeader(
    firstIcon: ImageVector? = null,
    endIcon: ImageVector? = null,
    modifier: Modifier = Modifier,
    withBorder: Boolean = true,
    middleContent: @Composable () -> Unit,
    firstIconOnclick: () -> Unit = { },
    endIconOnclick: () -> Unit = { }
) {
    if (withBorder) modifier.then(Modifier.border(2.dp, Color.DarkGray, RectangleShape))
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.then(
            Modifier
                .fillMaxHeight(.07f)
                .fillMaxWidth()
        )
    ) {
        if (firstIcon != null) {
            IconButton(
                onClick = firstIconOnclick,
            ) {
                Icon(
                    firstIcon, contentDescription = "Undo", modifier = Modifier
                        .scale(1.5f)
                        .padding(10.dp)
                )
            }
        }else Spacer(modifier = Modifier)

        middleContent()

        if (endIcon != null) {
            IconButton(
                onClick = endIconOnclick,
            ) {

                Icon(
                    endIcon, contentDescription = "Hint", modifier = Modifier
                        .scale(1.5f)
                        .padding(10.dp)
                )
            }
        }else Spacer(modifier = Modifier)
    }
}