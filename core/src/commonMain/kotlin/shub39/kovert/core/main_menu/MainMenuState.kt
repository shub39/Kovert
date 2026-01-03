package shub39.kovert.core.main_menu

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
@Immutable
data class MainMenuState(
    val ollamaUrl: String = "http://localhost:11434",
    val isValidUrl: Boolean = false
)