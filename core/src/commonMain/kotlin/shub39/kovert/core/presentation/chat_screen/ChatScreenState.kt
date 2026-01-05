package shub39.kovert.core.presentation.chat_screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import shub39.kovert.core.domain.ChatMessage
import shub39.kovert.core.domain.Mystery

@Stable
@Immutable
data class ChatScreenState(
    val chatMessages: List<ChatMessage> = listOf(),
    val mystery: Mystery? = null,
    val isLoadingNewMessage: Boolean = false,
    val isGameEnd: Boolean = false,
    val snackBarHostState: SnackbarHostState = SnackbarHostState()
)