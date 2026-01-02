package shub39.kovert.core.chat_screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.koin.compose.koinInject
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ChatScreenContent(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    state: ChatScreenState,
    onAction: (ChatScreenAction) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(state.snackBarHostState) }
    ) { padding ->
        LazyColumn(
            contentPadding = padding
        ) {
            item {
                Text(
                    text = "Chat Screen"
                )
                Button(
                    onClick = onNavigateUp
                ) {
                    Text("Go Back")
                }
            }

            item {
                if (state.mystery == null || state.isLoadingNewMessage) {
                    LinearWavyProgressIndicator()
                }
            }

            item {
                state.mystery?.let {
                    Text(
                        text = it.uiContext
                    )
                    Text(
                        text = it.persona.name
                    )
                    Text(
                        text = it.persona.front
                    )
                    Text(
                        text = it.hints.joinToString()
                    )
                }
            }

            item {
                state.chatMessages.forEach { message ->
                    Text(
                        text = "${message.sender}: ${message.content}"
                    )
                }
            }

            item {
                var newMessage by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = newMessage,
                    onValueChange = { newMessage = it }
                )

                Button(
                    onClick = {
                        onAction(ChatScreenAction.SendMessage(newMessage))
                        newMessage = ""
                    },
                    enabled = newMessage.isNotBlank() && !state.isLoadingNewMessage
                ) {
                    Text(text = "Send Message")
                }
            }
        }
    }
}