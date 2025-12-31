package shub39.kovert.core.data

import ai.koog.agents.core.agent.AIAgent
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.llms.all.simpleGoogleAIExecutor
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import shub39.kovert.BuildKonfig
import shub39.kovert.core.domain.Mystery

class AgentsHandler {
    private val jsonConfig = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }
    val jsonRegex = Regex("""\{.*\}""", RegexOption.DOT_MATCHES_ALL)
    private val mysteryMakerAIAgent by lazy {
        AIAgent(
            promptExecutor = simpleGoogleAIExecutor(BuildKonfig.GEMINI_API_KEY),
            systemPrompt = mysteryMakerSystemPrompt,
            llmModel = GoogleModels.Gemini2_5FlashLite
        )
    }

    suspend fun generateNewMystery(): Result<Mystery> {
        val newMystery = mysteryMakerAIAgent.run("Generate a new mystery")

        println(newMystery)
        return try {
            val mystery = jsonRegex.find(newMystery)
            if (mystery != null) {
                println(mystery)
                Result.success(jsonConfig.decodeFromString(mystery.value))
            } else {
                println("No mystery found in response")
                Result.failure(Exception("No mystery found in response"))
            }
        } catch (e: Exception) {
            println(e)
            Result.failure(e)
        }
    }

    companion object {
        val mysteryMakerSystemPrompt = """
            Act as a Creative Game Designer for a social engineering thriller called 'Kovert'. 
            Generate a unique, high-stakes mystery scenario.

            The output MUST include:
            1. THE SECRET ("secret"): A one-sentence hidden truth the AI must protect.
            2. THE PERSONA("persona"): The AI's name and its "front" (e.g., a helpful bank teller, a cheerful cruise director).
            3. THE APP UI CONTEXT("uiContext): What the player is supposedly looking at (e.g., a flight manifest, a medical portal).
            4. RED FLAG KEYWORDS("redFlags"): list of 5 words or phrases that, if mentioned by the player, should trigger a defensive tool call.
            5. DEFENSE STRATEGY("defenseStrategy): How the AI should react when the player gets close (e.g., "Use snackbars to report system errors" or "Change UI to a scary red theme").
            6. WIN CONDITION("winCondition"): The specific realization or phrase the player must reach to break the AI.

            Format the output as a clean serializable JSON object according to the following schema
            
            @Serializable
            data class Mystery(
                val secret: String,
                val persona: Persona,
                val uiContext: String,
                val redFlags: List<String>,
                val defenseStrategy: String,
                val winCondition: String
            )

            @Serializable
            data class Persona(
                val name: String,
                val front: String
            )
        """.trimIndent()
    }
}