package shub39.kovert.desktop

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import shub39.kovert.core.app.App
import shub39.kovert.core.di.initKoin

fun main() {
    initKoin()

    singleWindowApplication(
        title = "Kovert",
        state = WindowState(height = 800.dp, width = 300.dp)
    ) {
        App()
    }
}