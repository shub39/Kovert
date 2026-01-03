package shub39.kovert.core.data.agents

import ai.koog.agents.core.agent.AIAgentService
import ai.koog.agents.core.agent.GraphAIAgentService
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.prompt.llm.OllamaModels
import shub39.kovert.core.data.agents.AgentUtils.mysteryMakerSystemPrompt

typealias AIAgent = GraphAIAgentService<String, String>

class MysteryMakerAgentFactory {
    fun createAgent(ollamaUrl: String): AIAgent {
        return AIAgentService.Companion(
            promptExecutor = simpleOllamaAIExecutor(ollamaUrl),
            systemPrompt = mysteryMakerSystemPrompt,
            llmModel = OllamaModels.Meta.LLAMA_3_2_3B
        )
    }

//    suspend fun generateNewMystery(): Result<Mystery, Errors.AIErrors> {
//        val newMystery = mysteryMakerAIAgent.createAgentAndRun("Generate a new mystery")
//
//        println(newMystery)
//        return try {
//            val mystery = jsonRegex.find(newMystery)
//            if (mystery != null) {
//                Result.Success(jsonConfig.decodeFromString(mystery.value))
//            } else {
//                println("Can't extract json object")
//                Result.Error(Errors.AIErrors.PARSE_ERROR)
//            }
//        } catch (e: SerializationException) {
//            println("Can't deserialize json")
//            Result.Error(Errors.AIErrors.PARSE_ERROR, e.toString())
//        } catch (e: Exception) {
//            println("Unknown error ${e.message}")
//            Result.Error(Errors.AIErrors.UNKNOWN_ERROR, e.toString())
//        }
//    }
}