package logan.blockpartycompose.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import logan.blockpartycompose.R

@Composable
fun EmptyStar(scale: Float = 2.75f) {
    Icon(
        Icons.Outlined.Star, contentDescription = stringResource(R.string.empty_star), tint = Color.Gray,
        modifier = Modifier
            .padding(5.dp)
            .scale(scale)
    )
}

@Composable
fun FilledStar(scale: Float = 2.75f) {
    Icon(
        Icons.Filled.Star, contentDescription = stringResource(R.string.star),
        modifier = Modifier
            .padding(5.dp)
            .scale(scale)
    )
}
