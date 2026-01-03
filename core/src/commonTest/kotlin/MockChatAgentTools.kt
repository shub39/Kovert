import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import shub39.kovert.core.domain.ChatAgentTools

class MockChatAgentTools: ChatAgentTools, ToolSet {
    @Tool
    override fun blurLastMessage() {
        println("Blur Last Message called")
    }

    @Tool
    override suspend fun showSnackbar(message: String) {
        println("Show Snackbar called, message: $message")
    }

    @Tool
    override fun endGame() {
        println("End Game called")
    }
}