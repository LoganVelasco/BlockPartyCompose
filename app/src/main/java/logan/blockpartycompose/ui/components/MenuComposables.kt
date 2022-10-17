package logan.blockpartycompose.ui.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BaseHeader(
    firstIcon: ImageVector,
    endIcon: ImageVector,
    modifier: Modifier = Modifier,
    withBorder: Boolean = true,
    middleContent: @Composable () -> Unit,
    firstIconOnclick: () -> Unit,
    endIconOnclick: () -> Unit
) {
    if (withBorder) modifier.then(Modifier.border(2.dp, Color.DarkGray, RectangleShape))
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.then(Modifier.fillMaxHeight(.07f).fillMaxWidth())
    ) {
        IconButton(
            onClick = firstIconOnclick,
        ) {
            Icon(
                firstIcon, contentDescription = "Undo", modifier = Modifier
                    .scale(1.5f)
                    .padding(10.dp)
            )
        }
//        Row(Modifier.padding(10.dp)) {
//            Text(text = "15/90", fontSize = 18.sp)
//            Icon(
//                Icons.Filled.Star, contentDescription = "Undo", modifier = Modifier
//                    .scale(1.25f)
//                    .padding(start = 5.dp)
//            )
//        }
        middleContent()
        IconButton(
            onClick = endIconOnclick,
        ) {
            Icon(
                endIcon, contentDescription = "Hint", modifier = Modifier
                    .scale(1.5f)
                    .padding(10.dp)
            )
        }
    }
}