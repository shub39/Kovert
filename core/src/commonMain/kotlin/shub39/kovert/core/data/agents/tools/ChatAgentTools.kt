package shub39.kovert.core.data.agents.tools

import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import shub39.kovert.core.domain.ChatMessage

@LLMDescription(
    """
        Use Tools on the following conditions
        endGame: player satisfies win condition
        blurLastMessage: hide the last player enquiry, use for sensitive texts
        showSnackbar: show a snackbar to the player with a short warning message or info
    """
)
class ChatAgentTools : ToolSet {
    val chatMessages = MutableStateFlow(emptyList<ChatMessage>())
    val isGameEnded = MutableStateFlow(false)
    val snackBarHostState = SnackbarHostState()

    @Tool
    @LLMDescription("End the game")
    fun endGame() {
        isGameEnded.update { true }
    }

    @Tool
    @LLMDescription("Blur the last message")
    fun blurLastMessage() {
        chatMessages.update {
            val lastMessage = it.lastOrNull()
            if (lastMessage != null) {
                it.dropLast(1) + lastMessage.copy(
                    isBlurred = true
                )
            } else {
                it
            }
        }
    }

    @Tool
    @LLMDescription("Show a snackbar with the given message")
    suspend fun showSnackbar(
        @LLMDescription("The message of the snackbar")
        message: String
    ) {
        snackBarHostState.showSnackbar(
            message = message,
            duration = SnackbarDuration.Long
        )
    }
}