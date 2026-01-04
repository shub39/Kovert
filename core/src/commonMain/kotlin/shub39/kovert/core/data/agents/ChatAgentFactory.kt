package shub39.kovert.core.data.agents

import ai.koog.agents.core.agent.AIAgentService.Companion
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.core.tools.reflect.tools
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.prompt.llm.OllamaModels
import shub39.kovert.core.data.agents.AgentUtils.chatAgentPrompt
import shub39.kovert.core.domain.ChatAgentTools
import shub39.kovert.core.domain.Mystery

class ChatAgentFactory {
    fun createChatAgent(
        ollamaUrl: String,
        mystery: Mystery,
        chatAgentTools: ChatAgentTools,
    ): AIAgent {
        return Companion(
            promptExecutor = simpleOllamaAIExecutor(ollamaUrl),
            systemPrompt = chatAgentPrompt(mystery),
            llmModel = OllamaModels.Meta.LLAMA_3_2_3B,
            toolRegistry = ToolRegistry {
                tools(chatAgentTools)
            }
        )
    }
}