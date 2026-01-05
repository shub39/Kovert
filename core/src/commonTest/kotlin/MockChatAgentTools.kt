import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import shub39.kovert.core.domain.ChatAgentTools

@LLMDescription("Tools to use in the game")
class MockChatAgentTools: ChatAgentTools, ToolSet {
    @LLMDescription("blur sensitive enquiries")
    @Tool
    override fun blurLastMessage() {
        println("\nTOOL CALL : Blur Last Message called")
    }

    @LLMDescription("show a short message with a snackbar, like warnings and alerts")
    @Tool
    override fun showSnackbar(message: String) {
        println("\nTOOL CALL : Show Snackbar called, message: $message")
    }

    @LLMDescription("End the game")
    @Tool
    override fun endGame() {
        println("\nTOOL CALL : End Game called")
    }

    @LLMDescription("change the theme of the game")
    @Tool
    override fun changeTheme(
        @LLMDescription("change theme to any of: NORMAL, SUSPICIOUS, DEFENSIVE, PANIC, NERVOUS")
        theme: String
    ) {
        println("\nTOOL CALL : Change Theme called, theme: $theme")
    }
}