package shub39.kovert.core.viewmodels

import ai.koog.agents.core.agent.GraphAIAgentService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import shub39.kovert.core.chat_screen.ChatScreenAction
import shub39.kovert.core.chat_screen.ChatScreenState
import shub39.kovert.core.data.agents.ChatAgentFactory
import shub39.kovert.core.data.agents.MysteryFactory
import shub39.kovert.core.data.agents.tools.ChatAgentToolsImpl
import shub39.kovert.core.domain.ChatMessage
import shub39.kovert.core.domain.Entity
import shub39.kovert.core.domain.KovertDatastore
import shub39.kovert.core.domain.Result

class ChatScreenViewModel(
    private val datastore: KovertDatastore,
    private val chatAgentTools: ChatAgentToolsImpl,
    private val mysteryFactory: MysteryFactory,
    private val chatAgentFactory: ChatAgentFactory
) : ViewModel() {
    private var collectStateJob: Job? = null

    private var _chatAgent: GraphAIAgentService<String, String>? = null

    private val _state: MutableStateFlow<ChatScreenState> = MutableStateFlow(
        ChatScreenState(
            snackBarHostState = chatAgentTools.snackBarHostState,
        )
    )
    val state = _state.asStateFlow()
        .onStart {
            collectState()
            setupAgentsAndMystery()

            chatAgentTools.chatMessages.update { emptyList() }
            chatAgentTools.isGameEnded.update { false }
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
                chatAgentTools.chatMessages.update {
                    it + ChatMessage(Entity.USER, action.message)
                }

                viewModelScope.launch {
                    runAgentWithContext()
                }
            }
        }
    }

    private suspend fun runAgentWithContext() {
        val prompt = buildConversationPrompt()
        _chatAgent?.createAgentAndRun(prompt)?.let { response ->
            _state.update {
                it.copy(isLoadingNewMessage = false)
            }
            chatAgentTools.chatMessages.update {
                it + ChatMessage(Entity.AI_AGENT, response)
            }
        }
    }

    private fun buildConversationPrompt(): String {
        val history = _state.value.chatMessages.takeLast(10)
        return history.joinToString("\n") { msg ->
            when (msg.sender) {
                Entity.USER -> "PLAYER: ${msg.content}"
                Entity.AI_AGENT -> "YOU: ${msg.content}"
            }
        }
    }

    private suspend fun setupAgentsAndMystery() {
        val ollamaUrl = datastore.getOllamaUrl().first()

        when (val newMystery = mysteryFactory.generateMystery(ollamaUrl)) {
            is Result.Error -> {
                println("Could not create new mystery $newMystery")
            }

            is Result.Success -> {
                _chatAgent = chatAgentFactory.createChatAgent(
                    ollamaUrl = ollamaUrl,
                    mystery = newMystery.data,
                    chatAgentTools = chatAgentTools
                )
                _state.update { it.copy(mystery = newMystery.data) }
            }
        }
    }

    private fun collectState() {
        collectStateJob?.cancel()
        collectStateJob = viewModelScope.launch {
            combine(
                chatAgentTools.chatMessages,
                chatAgentTools.isGameEnded
            ) { chatMessages, isGameEnd ->
                _state.update {
                    it.copy(
                        chatMessages = chatMessages,
                        isGameEnd = isGameEnd
                    )
                }
            }.launchIn(this)
        }
    }
}