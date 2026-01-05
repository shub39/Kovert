package shub39.kovert.core.presentation.main_menu

import shub39.kovert.core.domain.MysteryData

sealed interface MainMenuAction {
    data class OnEditUrl(val url: String): MainMenuAction
    data object OnLoadNewData: MainMenuAction
    data class OnLoadMysteryData(val mysteryData: MysteryData): MainMenuAction
    data class OnDeleteMysteryData(val mysteryData: MysteryData): MainMenuAction
}