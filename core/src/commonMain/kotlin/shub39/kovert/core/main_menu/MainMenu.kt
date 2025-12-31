package shub39.kovert.core.main_menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mikepenz.hypnoticcanvas.shaderBackground
import com.mikepenz.hypnoticcanvas.shaders.MeshGradient
import kovert.core.generated.resources.Res
import kovert.core.generated.resources.kovert
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainMenu(
    modifier: Modifier = Modifier,
    onNavigateToChat: () -> Unit
) {
    val backgroundColors = listOf(
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.secondaryContainer,
        MaterialTheme.colorScheme.tertiaryContainer
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .shaderBackground(
                MeshGradient(
                    colors = backgroundColors.toTypedArray()
                ),
                fallback = {
                    Brush.verticalGradient(
                        colors = backgroundColors
                    )
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background.copy(0.5f),
                    shape = MaterialTheme.shapes.large
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(Res.string.kovert),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                Button(
                    onClick = onNavigateToChat
                ) {
                    Text(text = "Play")
                }
            }
        }
    }
}