package shub39.kovert.core.main_menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.materialkolor.DynamicMaterialTheme
import com.mikepenz.hypnoticcanvas.shaderBackground
import com.mikepenz.hypnoticcanvas.shaders.MeshGradient
import kovert.core.generated.resources.Res
import kovert.core.generated.resources.enter_ollama_url
import kovert.core.generated.resources.kovert
import kovert.core.generated.resources.kovert_desc
import kovert.core.generated.resources.new_game
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainMenu(
    modifier: Modifier = Modifier,
    onNavigateToChat: () -> Unit,
    state: MainMenuState,
    onAction: (MainMenuAction) -> Unit
) {
    val backgroundColors = listOf(
        Color(0xFFB7BA26),
        Color(0xFFFABD2F),
        Color(0xFFFE8019)
    )

    DynamicMaterialTheme(
        seedColor = Color(0xFFB7BA26),
        isDark = true
    ) {
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
            Card(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .widthIn(max = 500.dp)
                    .wrapContentSize(),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.kovert),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        text = stringResource(Res.string.kovert_desc)
                    )

                    OutlinedTextField(
                        value = state.ollamaUrl,
                        onValueChange = { onAction(MainMenuAction.OnEditUrl(it)) },
                        label = { Text(text = stringResource(Res.string.enter_ollama_url)) },
                        singleLine = true,
                        shape = MaterialTheme.shapes.large
                    )

                    Button(
                        onClick = onNavigateToChat,
                        enabled = state.isValidUrl
                    ) {
                        Text(text = stringResource(Res.string.new_game))
                    }
                }
            }
        }
    }
}