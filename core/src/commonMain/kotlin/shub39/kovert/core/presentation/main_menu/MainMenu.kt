package shub39.kovert.core.presentation.main_menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.materialkolor.DynamicMaterialTheme
import com.mikepenz.hypnoticcanvas.shaderBackground
import com.mikepenz.hypnoticcanvas.shaders.MeshGradient
import kovert.core.generated.resources.Res
import kovert.core.generated.resources.app_icon
import kovert.core.generated.resources.check
import kovert.core.generated.resources.delete
import kovert.core.generated.resources.enter_ollama_url
import kovert.core.generated.resources.kovert
import kovert.core.generated.resources.kovert_desc
import kovert.core.generated.resources.load_game
import kovert.core.generated.resources.new_game
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
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
    var loadGamePicker by remember { mutableStateOf(false) }

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
                    },
                    speed = 2f
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
                    Image(
                        painter = painterResource(Res.drawable.app_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )

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
                        placeholder = { Text(text = "http://localhost:11434") },
                        singleLine = true,
                        shape = MaterialTheme.shapes.large
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(
                            onClick = onNavigateToChat,
                            enabled = state.isValidUrl
                        ) {
                            Text(text = stringResource(Res.string.new_game))
                        }

                        OutlinedButton(
                            onClick = { loadGamePicker = true },
                            enabled = state.allMysteryData.isNotEmpty()
                        ) {
                            Text(text = stringResource(Res.string.load_game))
                        }
                    }
                }
            }
        }

        if (loadGamePicker) {
            ModalBottomSheet(
                onDismissRequest = { loadGamePicker = false },
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 60.dp
                    ),
                    modifier = Modifier
                        .heightIn(max = 600.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(state.allMysteryData) { mysteryData ->
                        Card(
                            shape = MaterialTheme.shapes.large,
                            onClick = {
                                onAction(MainMenuAction.OnLoadMysteryData(mysteryData))
                                onNavigateToChat()
                                loadGamePicker = false
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = mysteryData.mystery.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.weight(1f)
                                )

                                if (mysteryData.isSolved) {
                                    Icon(
                                        painter = painterResource(Res.drawable.check),
                                        contentDescription = null
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        onAction(MainMenuAction.OnDeleteMysteryData(mysteryData))
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.delete),
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}