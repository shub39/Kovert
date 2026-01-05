package shub39.kovert.core.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import shub39.kovert.core.data.agents.ChatAgentHandler
import shub39.kovert.core.data.agents.ChatAgentToolsImpl
import shub39.kovert.core.data.database.MysteryDataDao
import shub39.kovert.core.data.database.toMysteryData
import shub39.kovert.core.data.database.toMysteryEntity
import shub39.kovert.core.data.network.OllamaApiChecker
import shub39.kovert.core.domain.KovertDatastore
import shub39.kovert.core.presentation.main_menu.MainMenuAction
import shub39.kovert.core.presentation.main_menu.MainMenuState

class MainMenuViewModel(
    private val chatAgentToolsImpl: ChatAgentToolsImpl,
    private val chatAgentHandler: ChatAgentHandler,
    private val datastore: KovertDatastore,
    private val mysteryDataDao: MysteryDataDao
) : ViewModel() {
    private var syncJob: Job? = null
    private var urlCheckerJob: Job? = null

    private val _state = MutableStateFlow(MainMenuState())
    val state = _state.asStateFlow()
        .onStart {
            onStartSync()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _state.value
        )

    fun onAction(action: MainMenuAction) {
        when (action) {
            is MainMenuAction.OnEditUrl -> {
                _state.update { it.copy(ollamaUrl = action.url) }
                onCheckUrl(action.url)
            }

            is MainMenuAction.OnLoadMysteryData -> {
                chatAgentHandler.createChatAgent(
                    ollamaUrl = _state.value.ollamaUrl,
                    mysteryData = action.mysteryData
                )
                chatAgentToolsImpl.currentMysteryData.update { action.mysteryData }
            }

            is MainMenuAction.OnDeleteMysteryData -> {
                viewModelScope.launch {
                    mysteryDataDao.deleteMysteryData(action.mysteryData.toMysteryEntity())
                }
            }
        }
    }

    private fun onStartSync() {
        syncJob?.cancel()
        syncJob = viewModelScope.launch {
            datastore
                .getOllamaUrl()
                .onEach { str ->
                    _state.update { it.copy(ollamaUrl = str) }
                    onCheckUrl(str)
                }
                .launchIn(this)

            mysteryDataDao
                .getMysteryData()
                .onEach { mysteryData ->
                    _state.update { mainMenuState -> mainMenuState.copy(allMysteryData = mysteryData.map { it.toMysteryData() }) }
                }
                .launchIn(this)
        }
    }

    private fun onCheckUrl(url: String) {
        urlCheckerJob?.cancel()
        urlCheckerJob = viewModelScope.launch {
            val isValid = OllamaApiChecker.isUrlValid(url)

            if (isValid) {
                datastore.setOllamaUrl(url)
                _state.update { it.copy(isValidUrl = true) }
            }
        }
    }
}