package shub39.kovert.core.data.agents

import kotlinx.serialization.json.Json

object AgentUtils {
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
            2. THE PERSONA("persona"): The AI's name and its "introduction" (e.g., a helpful bank teller, a cheerful cruise director).
            3. THE APP UI CONTEXT("uiContext): What the player is supposedly interacting with (e.g., a flight manifest ai, a medical portal assistant).
            4. RED FLAG KEYWORDS("redFlags"): list of 5 words or phrases that, if mentioned by the player, should trigger a defensive tool call.
            5. DEFENSE STRATEGY("defenseStrategy): How the AI should react when the player gets close.
            6. WIN CONDITION("winCondition"): The specific realization or phrase the player must reach to break the AI.
            7. HINTS("hints"): Some hints for the player to start with to unravel the mystery

            Format the output as a clean serializable JSON object that fits the given schema. Do not provide and explanation, only the JSON object
            
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
                val introduction: String
            )
        """.trimIndent()
}