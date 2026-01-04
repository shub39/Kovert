import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import shub39.kovert.core.domain.ChatAgentTools

@LLMDescription("Tools to use in the game")
class MockChatAgentTools: ChatAgentTools, ToolSet {
    @LLMDescription("blur sensitive enquiries")
    @Tool
    override fun blurLastMessage() {
        println("\n TOOL CALL : Blur Last Message called")
    }

    @LLMDescription("show a short message with a snackbar, like warnings and alerts")
    @Tool
    override suspend fun showSnackbar(message: String) {
        println("\n TOOL CALL : Show Snackbar called, message: $message")
    }

    @LLMDescription("End the game")
    @Tool
    override fun endGame() {
        println("\n TOOL CALL : End Game called")
    }
}