package shub39.kovert.core.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import org.koin.compose.koinInject
import shub39.kovert.core.presentation.chat_screen.ChatScreen
import shub39.kovert.core.presentation.main_menu.MainMenu
import shub39.kovert.core.viewmodels.ChatScreenViewModel
import shub39.kovert.core.viewmodels.MainMenuViewModel

@Composable
fun App() {
    val backStack = rememberNavBackStack(Routes.config, Routes.MainMenu)

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) backStack.removeLastOrNull()
        },
        entryProvider = entryProvider {
            entry<Routes.MainMenu> {
                val viewmodel = koinInject<MainMenuViewModel>()
                val state by viewmodel.state.collectAsState()

                MainMenu(
                    state = state,
                    onAction = viewmodel::onAction,
                    onNavigateToChat = { backStack.add(Routes.ChatScreen) }
                )
            }
            entry<Routes.ChatScreen> {
                val viewmodel = koinInject<ChatScreenViewModel>()
                val state by viewmodel.state.collectAsState()

                ChatScreen(
                    state = state,
                    onAction = viewmodel::onAction,
                    onNavigateUp = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}