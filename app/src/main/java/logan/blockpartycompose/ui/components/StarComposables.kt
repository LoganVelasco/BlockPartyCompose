package logan.blockpartycompose.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import logan.blockpartycompose.R

@Composable
fun EmptyStar(scale: Float = 2.75f) {
    Icon(
        painter = painterResource(id = R.drawable.star),
        contentDescription = stringResource(R.string.empty_star), tint = Color.Gray,
        modifier = Modifier
            .padding(5.dp)
//            .scale(scale)
    )
}

@Composable
fun FilledStar(scale: Float = 2.75f) {
    Icon(
        painter = painterResource(id = R.drawable.star),
        contentDescription = stringResource(R.string.star),
        tint= Color.Unspecified,
        modifier = Modifier
            .padding(5.dp)
//            .scale(scale)
    )
}
