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
import shub39.kovert.core.data.network.OllamaApiChecker
import shub39.kovert.core.domain.KovertDatastore
import shub39.kovert.core.presentation.main_menu.MainMenuAction
import shub39.kovert.core.presentation.main_menu.MainMenuState

class MainMenuViewModel(
    private val datastore: KovertDatastore
) : ViewModel() {
    private var datastoreSyncJob: Job? = null
    private var urlCheckerJob: Job? = null

    private val _state = MutableStateFlow(MainMenuState())
    val state = _state.asStateFlow()
        .onStart {
            onStartDatastoreSync()
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
        }
    }

    private fun onStartDatastoreSync() {
        datastoreSyncJob?.cancel()
        datastoreSyncJob = viewModelScope.launch {
            datastore
                .getOllamaUrl()
                .onEach { str ->
                    _state.update { it.copy(ollamaUrl = str) }
                    onCheckUrl(str)
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