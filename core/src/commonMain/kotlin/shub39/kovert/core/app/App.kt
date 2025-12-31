package shub39.kovert.core.app

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay

@Composable
fun App() {
    val backStack = rememberNavBackStack(Routes.config, Routes.MainMenu)

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        },
        entryProvider = entryProvider {
            entry<Routes.MainMenu> {

            }
            entry<Routes.ChatScreen> {

            }
        }
    )
}