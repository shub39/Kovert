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

    @LLMDescription(
        """
            Hide the player's last message. 
            Use ONLY when the player asks a highly revealing or dangerous question. 
            Do NOT explain or justify this action.
        """
    )
    @Tool
    override fun blurLastMessage() {
        println("TOOL CALLED: blurLastMessage")
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

    @LLMDescription(
        """
            Show a very short warning or emotional reaction (under 10 words). 
            Use sparingly. Do NOT repeat messages. Do NOT explain context.
        """
    )
    @Tool
    override fun showSnackbar(
        @LLMDescription("Short message shown to the player")
        message: String
    ) {
        println("TOOL CALLED: showSnackbar, message: $message")
        scope.launch {
            snackBarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Long
            )
        }
    }

    @LLMDescription(
        """
            Mark the mystery as solved. 
            Call ONLY when the player clearly states the correct secret. 
            Never call on guesses or questions.
        """
    )
    @Tool
    override fun endGame() {
        println("TOOL CALLED: endGame")
        if (currentMysteryData.value == null) return

        currentMysteryData.update {
            it!!.copy(
                isSolved = true
            )
        }
    }

    @LLMDescription(
        """
            Change emotional state of the character. 
            Only change when player pressure increases or decreases. 
            Never change theme randomly."
        """
    )
    @Tool
    override fun changeTheme(
        @LLMDescription("One of: NORMAL, SUSPICIOUS, DEFENSIVE, PANIC, NERVOUS")
        theme: String
    ) {
        println("TOOL CALLED: changeTheme, theme: $theme")
        try {
            val orb = ChatOrb.entries.find { it.desc == theme } ?: throw Exception("Invalid theme")
            chatOrb.update { orb }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}