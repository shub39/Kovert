package shub39.kovert.core.data.agents.tools

import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import shub39.kovert.core.domain.ChatMessage

@LLMDescription("Tools to manipulate chat messages")
class ChatTools: ToolSet {
    val chatMessages = MutableStateFlow(emptyList<ChatMessage>())

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
}