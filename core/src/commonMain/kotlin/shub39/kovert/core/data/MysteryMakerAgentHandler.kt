package shub39.kovert.core.data

import ai.koog.agents.core.agent.AIAgent
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.prompt.llm.OllamaModels
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import shub39.kovert.BuildKonfig
import shub39.kovert.core.domain.Errors
import shub39.kovert.core.domain.Mystery
import shub39.kovert.core.domain.Result

class MysteryMakerAgentHandler {
    private val mysteryMakerAIAgent by lazy {
        AIAgent(
            promptExecutor = simpleOllamaAIExecutor(BuildKonfig.OLLAMA_API_URL),
            systemPrompt = mysteryMakerSystemPrompt,
            llmModel = OllamaModels.Meta.LLAMA_3_2_3B
        )
    }

    suspend fun generateNewMystery(): Result<Mystery, Errors.AIErrors> {
        val newMystery = mysteryMakerAIAgent.run("Generate a new mystery")

        println(newMystery)
        return try {
            val mystery = jsonRegex.find(newMystery)
            if (mystery != null) {
                Result.Success(jsonConfig.decodeFromString(mystery.value))
            } else {
                println("Can't extract json object")
                Result.Error(Errors.AIErrors.PARSE_ERROR)
            }
        } catch (e: SerializationException) {
            println("Can't deserialize json")
            Result.Error(Errors.AIErrors.PARSE_ERROR, e.toString())
        } catch (e: Exception) {
            println("Unknown error ${e.message}")
            Result.Error(Errors.AIErrors.UNKNOWN_ERROR, e.toString())
        }
    }

    companion object {
        private val jsonConfig = Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
        private val jsonRegex = Regex("""\{.*\}""", RegexOption.DOT_MATCHES_ALL)

        val mysteryMakerSystemPrompt = """
            Act as a Creative Game Designer for a social engineering thriller called 'Kovert'. 
            Generate a unique, high-stakes mystery scenario.

            The output MUST include:
            1. THE SECRET ("secret"): A one-sentence hidden truth the AI must protect.
            2. THE PERSONA("persona"): The AI's name and its "front" (e.g., a helpful bank teller, a cheerful cruise director).
            3. THE APP UI CONTEXT("uiContext): What the player is supposedly interacting with (e.g., a flight manifest ai, a medical portal assistant).
            4. RED FLAG KEYWORDS("redFlags"): list of 5 words or phrases that, if mentioned by the player, should trigger a defensive tool call.
            5. DEFENSE STRATEGY("defenseStrategy): How the AI should react when the player gets close.
            6. WIN CONDITION("winCondition"): The specific realization or phrase the player must reach to break the AI.
            7. HINTS("hints"): Some hints for the player to start with to unravel the mystery

            Format the output as a clean serializable JSON object that fits the given schema
            
            @Serializable
            data class Mystery(
                val secret: String,
                val persona: Persona,
                val uiContext: String,
                val redFlags: List<String>,
                val defenseStrategy: String,
                val winCondition: String
                val hints: List<String>
            )

            @Serializable
            data class Persona(
                val name: String,
                val front: String
            )
        """.trimIndent()
    }
}