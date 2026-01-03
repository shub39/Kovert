package shub39.kovert.core.data.agents

import ai.koog.agents.core.agent.AIAgentService
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.prompt.llm.OllamaModels
import shub39.kovert.core.data.agents.AgentUtils.mysteryMakerSystemPrompt

class MysteryMakerAgentFactory {
    fun createAgent(ollamaUrl: String): AIAgent {
        return AIAgentService.Companion(
            promptExecutor = simpleOllamaAIExecutor(ollamaUrl),
            systemPrompt = mysteryMakerSystemPrompt,
            llmModel = OllamaModels.Meta.LLAMA_3_2_3B
        )
    }
}