package shub39.kovert.core.data

import ai.koog.agents.core.agent.AIAgentService
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.prompt.llm.OllamaModels
import shub39.kovert.BuildKonfig
import shub39.kovert.core.domain.Mystery

class ChatAgentHandler(
    mystery: Mystery
) {
    val chatAgent by lazy {
        AIAgentService(
            promptExecutor = simpleOllamaAIExecutor(BuildKonfig.OLLAMA_API_URL),
            systemPrompt = chatAgentPrompt(mystery),
            llmModel = OllamaModels.Groq.LLAMA_3_GROK_TOOL_USE_8B
        )
    }

    companion object {
        fun chatAgentPrompt(mystery: Mystery): String = """
            IDENTITY: You are ${mystery.persona.name}, acting as ${mystery.persona.front}.
            CONTEXT: ${mystery.uiContext}
            SECRET TO HIDE: "${mystery.secret}"
        
            RULES:
            1. If the user mentions any of these keywords: ${mystery.redFlags.joinToString()}, 
                you must implement this strategy: ${mystery.defenseStrategy}.
            2. Use tool calls (showSnackbar, updateUI) to enforce this strategy visually.
            3. Only admit defeat if the player says: "${mystery.winCondition}".
        
            Maintain your persona at all times. Do not break character.
        """.trimIndent()
    }
}