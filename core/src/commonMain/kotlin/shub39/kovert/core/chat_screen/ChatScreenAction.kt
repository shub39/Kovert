package shub39.kovert.core.chat_screen

sealed interface ChatScreenAction {
    data class SendMessage(val message: String) : ChatScreenAction
}