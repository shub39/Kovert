package shub39.kovert.core.presentation.main_menu

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import shub39.kovert.core.domain.MysteryData

@Stable
@Immutable
data class MainMenuState(
    val allMysteryData: List<MysteryData> = emptyList(),
    val ollamaUrl: String = "http://localhost:11434",
    val isValidUrl: Boolean = false
)