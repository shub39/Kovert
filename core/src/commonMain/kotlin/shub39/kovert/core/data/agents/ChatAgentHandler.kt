package shub39.kovert.core.data.agents

import ai.koog.agents.core.agent.AIAgentService
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.core.tools.reflect.tools
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.prompt.llm.OllamaModels
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import shub39.kovert.BuildKonfig
import shub39.kovert.core.data.agents.tools.ChatTools
import shub39.kovert.core.data.agents.tools.SnackBarTools
import shub39.kovert.core.domain.Mystery

class ChatAgentHandler(
    mystery: Mystery,
) : KoinComponent {
    val chatAgent by lazy {
        AIAgentService.Companion(
            promptExecutor = simpleOllamaAIExecutor(BuildKonfig.OLLAMA_API_URL),
            systemPrompt = chatAgentPrompt(mystery),
            llmModel = OllamaModels.Meta.LLAMA_3_2_3B,
            toolRegistry = ToolRegistry {
                tools(get<SnackBarTools>())
                tools(get<ChatTools>())
            }
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
            2. Use tool calls (SnackBarTools, ChatTools) to enforce this strategy visually.
            3. Only admit defeat if the player says: "${mystery.winCondition}".
            4. The player has the following hints to work upon: "${mystery.hints.joinToString()}"
            5. If the player starts with "Debug:" use appropriate tool calls as specified 
        
            Maintain your persona at all times. Do not break character. You must always provide a conversational response. Don't
            include tool calls in responses. 
        """.trimIndent()
    }
}