package shub39.kovert.core.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import shub39.kovert.core.data.agents.ChatAgentHandler
import shub39.kovert.core.data.agents.ChatAgentToolsImpl
import shub39.kovert.core.data.agents.MysteryFactory
import shub39.kovert.core.data.database.MysteryDataDao
import shub39.kovert.core.data.database.toMysteryEntity
import shub39.kovert.core.domain.ChatMessage
import shub39.kovert.core.domain.Entity
import shub39.kovert.core.domain.KovertDatastore
import shub39.kovert.core.domain.Result
import shub39.kovert.core.presentation.chat_screen.ChatScreenAction
import shub39.kovert.core.presentation.chat_screen.ChatScreenState

class ChatScreenViewModel(
    private val datastore: KovertDatastore,
    private val mysteryDataDao: MysteryDataDao,
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

            chatAgentTools.currentMysteryData.update {
                it?.copy(
                    chatMessages = emptyList()
                )
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

        viewModelScope.launch {
            chatAgentTools.currentMysteryData.value?.let {
                mysteryDataDao.upsertMysteryData(it.toMysteryEntity())
            }
        }
    }

    private fun buildConversationPrompt(): String {
        val history = _state.value.chatMessages.takeLast(10)
        return """
            MESSAGES COUNT: ${state.value.chatMessages.size}
            LAST 10 MESSAGES:
            ${
            history.joinToString("\n") {
                when (it.sender) {
                    Entity.PLAYER -> "PLAYER: ${it.content}"
                    Entity.AGENT -> "AGENT: ${it.content}"
                }
            }
        }}
        """.trimIndent()
    }

    private suspend fun setupAgentsAndMystery() {
        if (chatAgentHandler.chatAgent != null) return

        val ollamaUrl = datastore.getOllamaUrl().first()

        when (val newMysteryData = mysteryFactory.generateMystery(ollamaUrl)) {
            is Result.Error -> {
                println("Could not create new mystery $newMysteryData")
            }

            is Result.Success -> {
                chatAgentHandler.createChatAgent(
                    ollamaUrl = ollamaUrl,
                    mysteryData = newMysteryData.data
                )
                chatAgentTools.currentMysteryData.update { newMysteryData.data }
            }
        }
    }

    private fun collectState() {
        collectStateJob?.cancel()
        collectStateJob = viewModelScope.launch {
            chatAgentTools.currentMysteryData.collect { mysteryData ->
                if (mysteryData != null) {
                    _state.update {
                        it.copy(
                            chatMessages = mysteryData.chatMessages,
                            isGameEnd = mysteryData.isSolved,
                            mystery = mysteryData.mystery
                        )
                    }
                }
            }
        }
    }
}