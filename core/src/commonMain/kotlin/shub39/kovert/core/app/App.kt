package shub39.kovert.core.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.materialkolor.DynamicMaterialTheme
import org.koin.compose.viewmodel.koinViewModel
import shub39.kovert.core.chat_screen.ChatScreen
import shub39.kovert.core.main_menu.MainMenu
import shub39.kovert.core.viewmodels.ChatScreenViewModel

@Composable
fun App() {
    val backStack = rememberNavBackStack(Routes.config, Routes.MainMenu)

    DynamicMaterialTheme(
        seedColor = Color(0xFFB8BB26),
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = {
                if (backStack.size > 1) {
                    backStack.removeLastOrNull()
                }
            },
            entryProvider = entryProvider {
                entry<Routes.MainMenu> {
                    MainMenu(
                        onNavigateToChat = { backStack.add(Routes.ChatScreen) }
                    )
                }
                entry<Routes.ChatScreen> {
                    val viewModel = koinViewModel<ChatScreenViewModel>()
                    val state by viewModel.state.collectAsState()

                    ChatScreen(
                        onNavigateUp = { backStack.removeLastOrNull() },
                        state = state,
                        onAction = viewModel::onAction
                    )
                }
            }
        )
    }
}