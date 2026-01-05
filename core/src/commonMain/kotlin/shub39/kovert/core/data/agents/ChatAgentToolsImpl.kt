package shub39.kovert.core.data.agents

import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import shub39.kovert.core.domain.ChatAgentTools
import shub39.kovert.core.domain.MysteryData

@LLMDescription("Tools to use in the game")
class ChatAgentToolsImpl : ToolSet, ChatAgentTools {
    val currentMysteryData = MutableStateFlow<MysteryData?>(null)
    val snackBarHostState = SnackbarHostState()

    @LLMDescription("blur sensitive enquiries")
    @Tool
    override fun blurLastMessage() {
        if (currentMysteryData.value == null) return

        currentMysteryData.update {
            val lastMessage = it!!.chatMessages.lastOrNull()
            it.copy(
               chatMessages = if (lastMessage != null) {
                   it.chatMessages.dropLast(1) + lastMessage.copy(
                       isBlurred = true
                   )
               } else {
                   it.chatMessages
               }
            )
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
        if (currentMysteryData.value == null) return

        currentMysteryData.update {
            it!!.copy(
                isSolved = true
            )
        }
    }
}