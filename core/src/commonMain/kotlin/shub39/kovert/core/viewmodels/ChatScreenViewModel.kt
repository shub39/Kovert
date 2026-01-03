package shub39.kovert.core.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import shub39.kovert.core.chat_screen.ChatScreenAction
import shub39.kovert.core.chat_screen.ChatScreenState
import shub39.kovert.core.data.agents.AIAgent
import shub39.kovert.core.data.agents.MysteryMakerAgentFactory
import shub39.kovert.core.data.agents.tools.ChatTools
import shub39.kovert.core.data.agents.tools.GameFlowTools
import shub39.kovert.core.data.agents.tools.SnackBarTools
import shub39.kovert.core.domain.ChatMessage
import shub39.kovert.core.domain.Entity
import shub39.kovert.core.domain.KovertDatastore

class ChatScreenViewModel(
    private val datastore: KovertDatastore,
    snackBarTools: SnackBarTools,
    private val chatTools: ChatTools,
    private val gameFlowTools: GameFlowTools,
    private val mysteryMakerAgentFactory: MysteryMakerAgentFactory
) : ViewModel() {
    private var collectStateJob: Job? = null

    private var _mysteryMakerAgent: AIAgent? = null
    private var _chatAgent: AIAgent? = null

    private val _state: MutableStateFlow<ChatScreenState> = MutableStateFlow(
        ChatScreenState(
            snackBarHostState = snackBarTools.snackBarHostState,
        )
    )
    val state = _state.asStateFlow()
        .onStart {
            collectState()
            chatTools.chatMessages.update { emptyList() }

            mysteryMakerAgentFactory

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
                    it.copy(isLoadingNewMessage = true)
                }
                chatTools.chatMessages.update {
                    it + ChatMessage(Entity.USER, action.message)
                }

                viewModelScope.launch {
                    runAgentWithContext()
                }
            }
        }
    }

    private suspend fun runAgentWithContext() {
//        val prompt = buildConversationPrompt()
//        _chatAgent?.chatAgent?.createAgentAndRun(prompt)?.let { response ->
//            _state.update {
//                it.copy(isLoadingNewMessage = false)
//            }
//            chatTools.chatMessages.update {
//                it + ChatMessage(Entity.AI_AGENT, response)
//            }
//        }
    }

    private fun buildConversationPrompt(): String {
        val history = _state.value.chatMessages.takeLast(10)
        val historyText = history.joinToString("\n") { msg ->
            when (msg.sender) {
                Entity.USER -> "User: ${msg.content}, blurred = ${msg.isBlurred}"
                Entity.AI_AGENT -> "YOU: ${msg.content}"
            }
        }
        return """
            Conversation History, Last User query is the current query :
            
            $historyText
        """.trimIndent()
    }

    private fun collectState() {
        collectStateJob?.cancel()
        collectStateJob = viewModelScope.launch {
            launch {
                chatTools.chatMessages.collect { chatMessages ->
                    _state.update { it.copy(chatMessages = chatMessages) }
                }
            }

            launch {
                gameFlowTools.isGameEnded.collect { isGameEnd ->
                    _state.update { it.copy(isGameEnd = isGameEnd) }
                }
            }
        }
    }
}