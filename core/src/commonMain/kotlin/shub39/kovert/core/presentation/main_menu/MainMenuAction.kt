package shub39.kovert.core.presentation.main_menu

sealed interface MainMenuAction {
    data class OnEditUrl(val url: String): MainMenuAction
}