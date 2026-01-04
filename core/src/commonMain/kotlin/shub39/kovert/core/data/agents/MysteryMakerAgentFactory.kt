package shub39.kovert.core.data.agents

import ai.koog.agents.core.agent.AIAgentService
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.prompt.llm.LLMCapability
import ai.koog.prompt.llm.LLMProvider
import ai.koog.prompt.llm.LLModel
import shub39.kovert.core.data.agents.AgentUtils.mysteryMakerSystemPrompt

class MysteryMakerAgentFactory {
    fun createAgent(ollamaUrl: String): AIAgent {
        return AIAgentService.Companion(
            promptExecutor = simpleOllamaAIExecutor(ollamaUrl),
            systemPrompt = mysteryMakerSystemPrompt,
            llmModel = LLModel(
                provider = LLMProvider.Ollama,
                id = "gemma3:latest",
                capabilities = listOf(
                    LLMCapability.Temperature,
                    LLMCapability.Schema.JSON.Standard,
                    LLMCapability.Tools
                ),
                contextLength = 4096
            )
        )
    }
}