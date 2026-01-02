package shub39.kovert.core.chat_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.materialkolor.DynamicMaterialTheme
import kovert.core.generated.resources.Res
import kovert.core.generated.resources.hints
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import shub39.kovert.core.chat_screen.components.ChatScreenBottomBar
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
    DynamicMaterialTheme(
        seedColor = Color(0xFFCEEAD6),
        isDark = true
    ) {
        val nestedScroll = TopAppBarDefaults.enterAlwaysScrollBehavior()
        Scaffold(
            modifier = modifier
                .nestedScroll(nestedScroll.nestedScrollConnection),
            snackbarHost = { SnackbarHost(state.snackBarHostState) },
            topBar = {
                ChatScreenTopAppBar(
                    state = state,
                    onNavigateUp = onNavigateUp,
                    scrollBehavior = nestedScroll
                )
            },
            bottomBar = {
                ChatScreenBottomBar(
                    state = state,
                    onAction = onAction
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = padding.calculateTopPadding() + 16.dp,
                    bottom = padding.calculateBottomPadding() + 60.dp,
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
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    item {
                        OutlinedCard(
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
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
                }

                item {
                    state.chatMessages.forEach { message ->
                        Text(
                            text = "${message.sender}: ${message.content}",
                            modifier = Modifier.blur(
                                radius = if (message.isBlurred) 12.dp else 0.dp,
                                edgeTreatment = BlurredEdgeTreatment.Unbounded
                            )
                        )
                    }
                }
            }
        }
    }
}