package shub39.kovert.core.chat_screen

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
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.materialkolor.DynamicMaterialTheme
import kotlinx.coroutines.delay
import kovert.core.generated.resources.Res
import kovert.core.generated.resources.hints
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import shub39.kovert.core.chat_screen.components.ChatMessage
import shub39.kovert.core.chat_screen.components.ChatScreenToolBar
import shub39.kovert.core.chat_screen.components.ChatScreenTopAppBar
import shub39.kovert.core.viewmodels.ChatScreenViewModel

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    chatScreenViewModel: ChatScreenViewModel = koinInject(),
) {
    val state by chatScreenViewModel.state.collectAsState()

    ChatScreenContent(
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        state = state,
        onAction = chatScreenViewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ChatScreenContent(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    state: ChatScreenState,
    onAction: (ChatScreenAction) -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(state.chatMessages.size) {
        delay(100)
        if (state.chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(listState.layoutInfo.totalItemsCount - 1)
        }
    }

    DynamicMaterialTheme(
        seedColor = Color(0xFFCEEAD6),
        isDark = true
    ) {
        val nestedScroll = TopAppBarDefaults.enterAlwaysScrollBehavior()
        Scaffold(
            modifier = modifier
                .nestedScroll(nestedScroll.nestedScrollConnection)
                .imePadding(),
            snackbarHost = { SnackbarHost(state.snackBarHostState) },
            topBar = {
                ChatScreenTopAppBar(
                    state = state,
                    onNavigateUp = onNavigateUp,
                    scrollBehavior = nestedScroll
                )
            }
        ) { padding ->
            Box(
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = padding.calculateTopPadding() + 16.dp,
                        bottom = padding.calculateBottomPadding() + 240.dp,
                        start = padding.calculateLeftPadding(LocalLayoutDirection.current) + 16.dp,
                        end = padding.calculateEndPadding(LocalLayoutDirection.current) + 16.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (state.mystery == null) {
                        item {
                            LoadingIndicator(
                                modifier = Modifier.padding(32.dp)
                            )
                        }
                    }

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
                        ChatMessage(
                            chatMessage = message
                        )
                    }
                }

                ChatScreenToolBar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(
                            bottom = padding.calculateBottomPadding() + 32.dp,
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