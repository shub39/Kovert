package shub39.kovert.core.data.agents.tools

import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState

@LLMDescription("Tools for showing snackbar")
class SnackBarTools: ToolSet {
    val snackBarHostState = SnackbarHostState()

    @Tool
    @LLMDescription("Show a snackbar with the given message")
    suspend fun showSnackbar(
        @LLMDescription("The message of the snackbar")
        message: String
    ) {
        snackBarHostState.showSnackbar(
            message = message,
            duration = SnackbarDuration.Long
        )
    }
}