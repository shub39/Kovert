package shub39.kovert.core.data.agents

import ai.koog.agents.core.agent.AIAgentService
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.core.tools.reflect.tools
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.prompt.llm.OllamaModels
import shub39.kovert.core.data.agents.AgentUtils.chatAgentPrompt
import shub39.kovert.core.data.agents.tools.ChatTools
import shub39.kovert.core.data.agents.tools.GameFlowTools
import shub39.kovert.core.data.agents.tools.SnackBarTools
import shub39.kovert.core.domain.Mystery

class ChatAgentFactory {
    fun createChatAgent(
        ollamaUrl: String,
        mystery: Mystery,
        snackBarTools: SnackBarTools,
        chatTools: ChatTools,
        gameFlowTools: GameFlowTools
    ): AIAgent {
        return AIAgentService.Companion(
            promptExecutor = simpleOllamaAIExecutor(ollamaUrl),
            systemPrompt = chatAgentPrompt(mystery),
            llmModel = OllamaModels.Meta.LLAMA_3_2_3B,
            toolRegistry = ToolRegistry {
                tools(snackBarTools)
                tools(chatTools)
                tools(gameFlowTools)
            }
        )
    }
}