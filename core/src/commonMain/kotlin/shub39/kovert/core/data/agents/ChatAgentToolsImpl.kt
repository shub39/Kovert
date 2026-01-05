package shub39.kovert.core.data.agents

import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import shub39.kovert.core.domain.ChatAgentTools
import shub39.kovert.core.domain.ChatOrb
import shub39.kovert.core.domain.MysteryData

@LLMDescription("Tools to use in the game")
class ChatAgentToolsImpl : ToolSet, ChatAgentTools {
    val currentMysteryData = MutableStateFlow<MysteryData?>(null)
    val chatOrb = MutableStateFlow(ChatOrb.NORMAL)
    val snackBarHostState = SnackbarHostState()

    private val scope = CoroutineScope(Dispatchers.Default)

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
    override fun showSnackbar(
        @LLMDescription("the message of the snackbar")
        message: String
    ) {
        scope.launch {
            snackBarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Long
            )
        }
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

    @LLMDescription("change the theme of the game")
    @Tool
    override fun changeTheme(
        @LLMDescription("change theme to any of: NORMAL, SUSPICIOUS, DEFENSIVE, PANIC, NERVOUS")
        theme: String
    ) {
        try {
            val orb = ChatOrb.entries.find { it.desc == theme } ?: throw Exception("Invalid theme")
            chatOrb.update { orb }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}