package shub39.kovert.core.data.agents.tools

import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@LLMDescription("Tools for ending the game")
class GameFlowTools : ToolSet {
    val isGameEnded = MutableStateFlow(false)

    @Tool
    @LLMDescription("If player qualifies the win condition, end the game by calling this")
    fun endGame() {
        isGameEnded.update { true }
    }
}