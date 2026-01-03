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
        state = WindowState(height = 1000.dp, width = 500.dp)
    ) {
        App()
    }
}