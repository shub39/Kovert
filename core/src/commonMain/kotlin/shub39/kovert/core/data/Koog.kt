package shub39.kovert.core.data

import ai.koog.agents.core.agent.AIAgent
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.llms.all.simpleGoogleAIExecutor
import shub39.kovert.BuildKonfig

object Koog {
    suspend fun test(): String {
        val agent = AIAgent(
            promptExecutor = simpleGoogleAIExecutor(BuildKonfig.GEMINI_API_KEY),
            systemPrompt = "You are a helpful assistant. Answer user questions concisely.",
            llmModel = GoogleModels.Gemini2_5FlashLite
        )
        return agent.run("Hello! How can you help me?")
    }
}