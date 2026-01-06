import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import shub39.kovert.core.domain.ChatAgentTools

@LLMDescription("Tools to use in the game")
class MockChatAgentTools: ChatAgentTools, ToolSet {
    @LLMDescription(
        """
            Hide the player's last message. 
            Use ONLY when the player asks a highly revealing or dangerous question. 
            Do NOT explain or justify this action.
        """
    )
    @Tool
    override fun blurLastMessage() {
        println("\nTOOL CALL : Blur Last Message called")
    }

    @LLMDescription(
        """
            Show a very short warning or emotional reaction (under 10 words). 
            Use sparingly. Do NOT repeat messages. Do NOT explain context.
        """
    )
    @Tool
    override fun showSnackbar(
        @LLMDescription("Short message shown to the player")
        message: String
    ) {
        println("\nTOOL CALL : Show Snackbar called, message: $message")
    }

    @LLMDescription(
        """
            Mark the mystery as solved. 
            Call ONLY when the player clearly states the correct secret. 
            Never call on guesses or questions.
        """
    )
    @Tool
    override fun endGame() {
        println("\nTOOL CALL : End Game called")
    }

    @LLMDescription(
        """
            Change emotional state of the character. 
            Only change when player pressure increases or decreases. 
            Never change theme randomly."
        """
    )
    @Tool
    override fun changeTheme(
        @LLMDescription("One of: NORMAL, SUSPICIOUS, DEFENSIVE, PANIC, NERVOUS")
        theme: String
    ) {
        println("\nTOOL CALL : Change Theme called, theme: $theme")
    }
}