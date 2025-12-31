package shub39.kovert.core.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import shub39.kovert.core.chat_screen.ChatScreenAction
import shub39.kovert.core.chat_screen.ChatScreenState
import shub39.kovert.core.data.AgentsHandler
import shub39.kovert.core.domain.ChatMessage
import shub39.kovert.core.domain.Entity

class ChatScreenViewModel(
    private val agentsHandler: AgentsHandler
): ViewModel() {

    private val _state = MutableStateFlow(ChatScreenState())
    val state = _state.asStateFlow()
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
                        chatMessages = it.chatMessages + ChatMessage(
                            sender = Entity.USER,
                            content = action.message,
                        )
                    )
                }
            }
        }
    }
}