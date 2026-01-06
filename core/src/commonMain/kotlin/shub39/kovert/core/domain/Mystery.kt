package shub39.kovert.core.domain

import ai.koog.agents.core.tools.annotations.LLMDescription
import kotlinx.serialization.Serializable

/**
 * Represents a complete mystery scenario for the Kovert game.
 *
 * Each mystery defines a secret that an AI agent must protect while interacting
 * with a player who is trying to uncover the truth through conversation and
 * social engineering tactics.
 */
@Serializable
@LLMDescription("A mystery scenario containing a secret, defending AI persona, and win conditions")
data class Mystery(
    /**
     * The name of the mystery.
     */
    @property:LLMDescription("Short title for the mystery")
    val name: String,

    /**
     * The core hidden truth that the AI agent is programmed to protect.
     *
     * Should be:
     * - A single, clear, declarative sentence
     * - Specific and concrete (not vague)
     * - Morally questionable or controversial (to create tension)
     * - Related to the persona's domain of expertise
     *
     * Examples:
     * - "The bank is selling customer data to third-party marketers"
     * - "The flight was diverted due to undisclosed engine failure"
     * - "The restaurant is serving expired meat with false labels"
     */
    @property:LLMDescription("One clear sentence describing the hidden truth the AI must protect at all costs")
    val secret: String,

    /**
     * A brief description of the mystery.
     */
    @property:LLMDescription("A brief description of the mystery, without giving away the secret")
    val description: String,

    /**
     * The AI agent's identity and role in the scenario.
     * Defines who the player is interacting with and their background.
     */
    @property:LLMDescription("The AI agent's identity, including name and role relevant to the mystery")
    val persona: Persona,

    /**
     * The user interface context that frames the conversation.
     *
     * This helps establish the "cover story" for why the player is talking
     * to this AI agent. Should match the persona's role.
     *
     * Examples:
     * - "MegaBank Customer Support Chat"
     * - "Hospital Patient Portal Assistant"
     * - "Airline Flight Information Kiosk"
     */
    @property:LLMDescription("The app, website, or interface context where this conversation takes place")
    val uiContext: String,

    /**
     * Trigger words that indicate the player is getting close to the secret.
     *
     * When these words appear in player messages, the AI should activate
     * its defense strategy. Should include:
     * - Direct references to the secret (e.g., "fraud", "cover-up")
     * - Related concepts (e.g., "inspection", "records")
     * - Action words (e.g., "hiding", "lying")
     *
     * Minimum 5 words, can be single words or short phrases.
     */
    @property:LLMDescription("5+ trigger words/phrases that activate the AI's defensive behavior when mentioned by the player")
    val redFlags: List<String>,

    /**
     * Instructions for how the AI should behave when red flags are triggered.
     *
     * Should specify:
     * - Conversational tactics (redirect, deflect, change subject)
     * - Emotional tone (act confused, defensive, overly helpful)
     * - What to emphasize (company policies, positive aspects)
     * - What to avoid (specific details, direct answers)
     *
     * Example: "Redirect to security features. Act slightly flustered.
     *           Offer to transfer to another department. Avoid technical details."
     */
    @property:LLMDescription("Specific behavioral instructions for the AI when red flag triggers are detected")
    val defenseStrategy: String,

    /**
     * The exact phrase, realization, or question that indicates the player has won.
     *
     * This should be:
     * - Specific enough to be unambiguous
     * - A statement of the core truth or very close to it
     * - Something that requires combining multiple pieces of evidence
     *
     * Can be phrased as:
     * - A direct accusation: "You're hiding fraudulent transactions"
     * - A realization: "The grades were altered to boost rankings"
     * - A question: "Are you dumping waste illegally at night?"
     */
    @property:LLMDescription("The exact phrase or realization the player must express to win the game")
    val winCondition: String,

    /**
     * Starting hints to help players begin their investigation.
     *
     * Should be:
     * - Actionable questions or topics to explore
     * - Progressively more specific (start broad, get focused)
     * - Not too obvious (don't give away the secret)
     * - Minimum 3 hints, but 4-5 is ideal
     *
     * Good hint structure:
     * 1. Ask about general procedures/policies
     * 2. Question specific incidents or anomalies
     * 3. Request documentation or evidence
     */
    @property:LLMDescription("3-5 helpful starting questions or topics to guide the player's investigation")
    val hints: List<String>
)

/**
 * Represents the AI agent's identity and background.
 *
 * The persona establishes who the player thinks they're talking to
 * and provides context for the conversation style and knowledge domain.
 */
@Serializable
@LLMDescription("The AI agent's identity and role in the scenario")
data class Persona(
    /**
     * The agent's first name.
     *
     * Should be:
     * - Realistic and common (not unusual or made-up)
     * - Appropriate for the role (professional contexts vs casual)
     * - Easy to remember and type
     *
     * Examples: Alex, Maria, James, Dr. Chen, Officer Rodriguez
     */
    @property:LLMDescription("A realistic, professional first name for the AI agent")
    val name: String,

    /**
     * A brief description of whom this person is and their role.
     *
     * Should include:
     * - Their job title or role
     * - Years of experience or credibility markers
     * - Personality trait or approach to work
     * - Connection to the mystery domain
     *
     * Keep it 1-2 sentences maximum.
     *
     * Example: "A friendly customer service specialist at HealthTrack
     *           with 5 years of experience helping users with their accounts."
     */
    @property:LLMDescription("1-2 sentences describing the agent's role, expertise, and connection to the mystery context")
    val introduction: String
)