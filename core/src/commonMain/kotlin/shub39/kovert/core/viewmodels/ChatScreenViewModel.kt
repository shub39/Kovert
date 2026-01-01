package shub39.kovert.core.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import shub39.kovert.core.chat_screen.ChatScreenAction
import shub39.kovert.core.chat_screen.ChatScreenState
import shub39.kovert.core.data.ChatAgentHandler
import shub39.kovert.core.data.MysteryMakerAgentHandler
import shub39.kovert.core.domain.ChatMessage
import shub39.kovert.core.domain.Entity
import shub39.kovert.core.domain.onSuccess

class ChatScreenViewModel(
    private val agentsHandler: MysteryMakerAgentHandler
) : ViewModel() {

    private var _chatAgentHandler: ChatAgentHandler? = null

    private val _state = MutableStateFlow(ChatScreenState())
    val state = _state.asStateFlow()
        .onStart {
            agentsHandler
                .generateNewMystery()
                .onSuccess { mystery ->
                    _state.update { chatScreenState ->
                        chatScreenState.copy(mystery = mystery)
                    }

                    _chatAgentHandler = ChatAgentHandler(mystery)
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _state.value
        )

    fun onAction(action: ChatScreenAction) {
        when (action) {
            is ChatScreenAction.SendMessage -> {
                _state.update {
                    it.copy(
                        isLoadingNewMessage = true,
                        chatMessages = it.chatMessages + ChatMessage(
                            sender = Entity.USER,
                            content = action.message,
                        )
                    )
                }

                viewModelScope.launch {
                    runAgentWithContext()
                }
            }
        }
    }

    private suspend fun runAgentWithContext() {
        val prompt = buildConversationPrompt()
        _chatAgentHandler?.chatAgent?.createAgentAndRun(prompt)?.let { response ->
            _state.update {
                it.copy(
                    chatMessages = it.chatMessages + ChatMessage(Entity.AI_AGENT, response),
                    isLoadingNewMessage = false
                )
            }
        }
    }

    private fun buildConversationPrompt(): String {
        val history = _state.value.chatMessages.takeLast(10)
        val historyText = history.joinToString("\n") { msg ->
            when (msg.sender) {
                Entity.USER -> "User: ${msg.content}"
                Entity.AI_AGENT -> "YOU: ${msg.content}"
            }
        }
        return """
            Conversation History, Last User query is the current query :
            
            $historyText
        """.trimIndent()
    }
}