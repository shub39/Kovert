package shub39.kovert.core.viewmodels

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
import shub39.kovert.core.data.agents.ChatAgentHandler
import shub39.kovert.core.data.agents.ChatAgentToolsImpl
import shub39.kovert.core.data.agents.MysteryFactory
import shub39.kovert.core.domain.ChatMessage
import shub39.kovert.core.domain.ChatOrb
import shub39.kovert.core.domain.Entity
import shub39.kovert.core.domain.KovertDatastore
import shub39.kovert.core.domain.MysteryDataRepo
import shub39.kovert.core.domain.Result
import shub39.kovert.core.presentation.chat_screen.ChatScreenAction
import shub39.kovert.core.presentation.chat_screen.ChatScreenState

class ChatScreenViewModel(
    private val datastore: KovertDatastore,
    private val repo: MysteryDataRepo,
    private val chatAgentTools: ChatAgentToolsImpl,
    private val mysteryFactory: MysteryFactory,
    private val chatAgentHandler: ChatAgentHandler
) : ViewModel() {
    private var collectStateJob: Job? = null

    private val _state: MutableStateFlow<ChatScreenState> = MutableStateFlow(
        ChatScreenState(
            snackBarHostState = chatAgentTools.snackBarHostState,
        )
    )
    val state = _state.asStateFlow()
        .onStart {
            collectState()
            setupAgentsAndMystery()
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
                chatAgentTools.currentMysteryData.update {
                    it?.copy(
                        chatMessages = it.chatMessages + ChatMessage(Entity.PLAYER, action.message)
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
        chatAgentHandler.chatAgent?.createAgentAndRun(prompt)?.let { response ->
            _state.update {
                it.copy(isLoadingNewMessage = false)
            }
            chatAgentTools.currentMysteryData.update {
                it?.copy(
                    chatMessages = it.chatMessages + ChatMessage(Entity.AGENT, response)
                )
            }
        }
    }

    private fun buildConversationPrompt(): String {
        val history = _state.value.chatMessages.takeLast(10)
        return """
            THEME: ${_state.value.chatOrb.desc} 
            LAST MESSAGES:
            ${history.joinToString("\n") {
                when (it.sender) {
                    Entity.PLAYER -> "PLAYER: ${it.content}"
                    Entity.AGENT -> "AGENT: ${it.content}"
                }
            }}}
        """.trimIndent()
    }

    private suspend fun setupAgentsAndMystery() {
        chatAgentTools.chatOrb.update { ChatOrb.NORMAL }

        if (chatAgentHandler.chatAgent != null) return

        val ollamaUrl = datastore.getOllamaUrl().first()

        when (val newMysteryData = mysteryFactory.generateMystery(ollamaUrl)) {
            is Result.Error -> {
                println("Could not create new mystery $newMysteryData")
            }

            is Result.Success -> {
                chatAgentHandler.createChatAgent(
                    ollamaUrl = ollamaUrl,
                    mystery = newMysteryData.data.mystery
                )
                chatAgentTools.currentMysteryData.update { newMysteryData.data }
            }
        }
    }

    private fun collectState() {
        collectStateJob?.cancel()
        collectStateJob = viewModelScope.launch {
            combine(
                chatAgentTools.chatOrb,
                chatAgentTools.currentMysteryData
            ) { chatOrb, mysteryData ->
                _state.update { it.copy(chatOrb = chatOrb) }

                if (mysteryData != null) {
                    _state.update {
                        it.copy(
                            chatMessages = mysteryData.chatMessages,
                            isGameEnd = mysteryData.isSolved,
                            mystery = mysteryData.mystery
                        )
                    }

                    repo.upsertMysteryData(mysteryData)
                }
            }.launchIn(this)
        }
    }
}