package shub39.kovert.core.presentation.chat_screen

sealed interface ChatScreenAction {
    data class SendMessage(val message: String) : ChatScreenAction
}