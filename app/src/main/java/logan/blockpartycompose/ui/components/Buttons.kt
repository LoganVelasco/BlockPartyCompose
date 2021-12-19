package logan.blockpartycompose.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlayButton(onNavigate: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onNavigate,
        modifier = modifier
            .padding(start = 75.dp, end = 75.dp, top = 100.dp)
    ) {
        Text(text = "Play")
    }
}


@Composable
fun DifficultyButton(
    onNavigate: () -> Unit,
    label: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onNavigate,
        enabled = enabled,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = label)
    }
}

@Composable
fun HeaderButton(onNavigate: () -> Unit, label: String, modifier: Modifier = Modifier) {
    Button(
        onClick = onNavigate,
        modifier = modifier
    ) {
        Text(
            text = label,
            fontSize = 12.sp
        )
    }
}