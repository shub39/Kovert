package shub39.kovert.core.data.agents

import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import shub39.kovert.core.domain.ChatAgentTools
import shub39.kovert.core.domain.ChatMessage

@LLMDescription("Tools to use in the game")
class ChatAgentToolsImpl : ToolSet, ChatAgentTools {
    val chatMessages = MutableStateFlow(emptyList<ChatMessage>())
    val isGameEnded = MutableStateFlow(false)
    val snackBarHostState = SnackbarHostState()

    @LLMDescription("blur sensitive enquiries")
    @Tool
    override fun blurLastMessage() {
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

    @LLMDescription("show a short message with a snackbar")
    @Tool
    override suspend fun showSnackbar(
        @LLMDescription("the message of the snackbar")
        message: String
    ) {
        snackBarHostState.showSnackbar(
            message = message,
            duration = SnackbarDuration.Long
        )
    }

    @LLMDescription("end the game")
    @Tool
    override fun endGame() {
        isGameEnded.update { true }
    }
}