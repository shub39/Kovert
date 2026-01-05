package shub39.kovert.core.presentation.chat_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.PaletteStyle
import kovert.core.generated.resources.Res
import kovert.core.generated.resources.hints
import org.jetbrains.compose.resources.stringResource
import shub39.kovert.core.presentation.chat_screen.components.ChatMessage
import shub39.kovert.core.presentation.chat_screen.components.ChatScreenToolBar
import shub39.kovert.core.presentation.chat_screen.components.ChatScreenTopBar
import shub39.kovert.core.presentation.chat_screen.components.TypingIndicator

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    state: ChatScreenState,
    onAction: (ChatScreenAction) -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(state.chatMessages.size, state.isLoadingNewMessage) {
        if (state.chatMessages.isNotEmpty() || state.isLoadingNewMessage) {
            listState.animateScrollToItem(listState.layoutInfo.totalItemsCount - 1)
        }
    }

    DynamicMaterialTheme(
        seedColor = state.chatOrb.colors.first(),
        style = if (state.mystery == null) PaletteStyle.Monochrome else PaletteStyle.Expressive,
        isDark = true
    ) {
        Scaffold(
            modifier = modifier.imePadding(),
            snackbarHost = { SnackbarHost(state.snackBarHostState) },
        ) { padding ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.padding(
                        top = padding.calculateTopPadding() + 16.dp,
                        bottom = padding.calculateBottomPadding()
                    )
                ) {
                    ChatScreenTopBar(
                        onNavigateUp = onNavigateUp,
                        state = state
                    )

                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .widthIn(max = 600.dp)
                            .fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = 16.dp,
                            bottom = 240.dp,
                            start = padding.calculateLeftPadding(LocalLayoutDirection.current) + 16.dp,
                            end = padding.calculateEndPadding(LocalLayoutDirection.current) + 16.dp
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        state.mystery?.let { mystery ->
                            item {
                                Card(
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Text(
                                        text = mystery.persona.introduction,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    )
                                }
                            }

                            item {
                                OutlinedCard(
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    ) {
                                        Text(
                                            text = stringResource(Res.string.hints),
                                            style = MaterialTheme.typography.titleSmallEmphasized
                                        )
                                        mystery.hints.forEach { hint ->
                                            Text(
                                                text = "- $hint",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(32.dp))
                            }
                        }

                        items(state.chatMessages) { message ->
                            ChatMessage(chatMessage = message)
                        }

                        if (state.isLoadingNewMessage) {
                            item { TypingIndicator() }
                        }
                    }
                }

                if (!state.isGameEnd) {
                    ChatScreenToolBar(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(
                                bottom = padding.calculateBottomPadding() + 16.dp,
                                start = padding.calculateLeftPadding(LocalLayoutDirection.current) + 16.dp,
                                end = padding.calculateEndPadding(LocalLayoutDirection.current) + 16.dp
                            ),
                        state = state,
                        onAction = onAction
                    )
                }
            }
        }
    }
}