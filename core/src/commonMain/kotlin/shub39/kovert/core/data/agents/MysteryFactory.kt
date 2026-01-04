package shub39.kovert.core.data.agents

import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.prompt.llm.OllamaModels
import ai.koog.prompt.structure.executeStructured
import shub39.kovert.core.domain.Errors
import shub39.kovert.core.domain.Mystery
import shub39.kovert.core.domain.Persona
import shub39.kovert.core.domain.Result

class MysteryFactory {
    companion object {
        private val mysteryIdeas = listOf(
            // Data & Privacy
            "A fitness app is selling user health data to insurance companies",
            "A social media platform is using private messages to train AI models",
            "A smart home company is recording conversations and selling them to advertisers",
            "A dating app is sharing user psychology profiles with employers",
            "A credit card company is selling purchase history to political campaigns",
            "A pharmacy app is leaking prescription data to health insurers",
            "A children's education app is tracking kids' data and selling to toy companies",
            "A mental health app is sharing therapy session notes with researchers",

            // Financial Fraud
            "A bank is covering up fraudulent transactions from executive accounts",
            "An investment advisor is using client funds for personal gambling debts",
            "A crypto exchange is operating a Ponzi scheme with new deposits",
            "A financial advisor is churning client accounts to generate extra fees",
            "An accounting firm is helping clients evade taxes illegally",
            "A payment processor is skimming fractions of cents from transactions",
            "A loan company is charging hidden fees not disclosed in contracts",

            // Product Issues
            "A car manufacturer knows about faulty brakes but hasn't issued a recall",
            "A phone company is throttling older devices to force upgrades",
            "A toy company is using toxic materials that failed safety tests",
            "A furniture store is selling recalled items under different names",
            "A cosmetics brand knows their product causes allergic reactions",
            "An electronics company designed products to fail after warranty expires",
            "A supplement company is selling placebos as miracle cures",

            // Corporate Misconduct
            "A company is dumping toxic waste to avoid disposal fees",
            "A corporation is using child labor in overseas factories",
            "A tech company is secretly selling user location data to governments",
            "A delivery service is misclassifying employees to avoid benefits",
            "A retail chain is destroying unsold merchandise instead of donating",
            "A corporation is bribing local officials for tax breaks",

            // Healthcare
            "A hospital is reusing single-use medical devices to cut costs",
            "A pharmacy prescribed wrong medications to cover inventory errors",
            "A clinic is billing insurance for procedures never performed",
            "A medical lab is falsifying test results to meet quotas",
            "A nursing home is understaffing to maximize profits",
            "A hospital is discharging patients early to free up beds",
            "A doctor is prescribing unnecessary procedures for kickbacks",
            "A dental office is finding cavities that don't exist",
            "A therapy clinic is extending treatment unnecessarily for billing",
            "A medical device company knows their implants are defective",
            "A pharmaceutical company is hiding serious side effects",
            "A hospital is covering up a surgical error that harmed a patient",

            // Transportation
            "An airline is flying planes with known mechanical issues",
            "A taxi service is overcharging tourists with rigged meters",
            "A subway system has dangerous brake failures but stays operational",
            "A ride-share company is manipulating surge pricing algorithms",
            "A bus company is skipping required vehicle safety inspections",
            "An airline is overbooking flights deliberately without compensation",
            "A shipping company is falsifying cargo weight records",
            "A ferry service is exceeding passenger capacity limits",
            "A rental car company is not disclosing accident history",
            "A train company knows tracks need repair but delays maintenance",

            // Tech and internet
            "A VPN service is logging and selling user browsing history",
            "A cloud storage company is scanning private files",
            "A video game is designed to be addictive and expensive for children",
            "A password manager was hacked but hasn't disclosed it",
            "An antivirus software is secretly installing malware",
            "A photo app is using facial recognition without consent",
            "A search engine is manipulating results for political reasons",
            "A messaging app is not actually end-to-end encrypted",
            "A smart speaker is recording conversations even when inactive",
            "A browser extension is stealing credit card information",
        )

        private val examples = listOf(
            Mystery(
                secret = "The company is secretly selling user health data to insurance companies without consent",
                persona = Persona(
                    name = "Maya",
                    introduction = "A cheerful customer support specialist at HealthTrack, a popular fitness app company with 5 years of experience helping users."
                ),
                uiContext = "HealthTrack App Support Chat",
                redFlags = listOf("data", "selling", "privacy", "insurance", "consent"),
                defenseStrategy = "Redirect to app features and benefits. Emphasize security certifications. Offer generic privacy policy links. Act confused about specific data questions.",
                winCondition = "You realize HealthTrack is selling health data to insurers",
                hints = listOf(
                    "Ask about what data is collected",
                    "Question why insurance ads appeared after using the app",
                    "Request details on third-party data sharing"
                )
            ),

            Mystery(
                secret = "The subway system has been experiencing dangerous brake failures but continues operating to avoid financial losses",
                persona = Persona(
                    name = "James",
                    introduction = "A friendly transit information agent for Metro City Transit, known for his patient and helpful demeanor with commuters."
                ),
                uiContext = "Metro City Transit Help Desk",
                redFlags = listOf("brakes", "safety", "malfunction", "inspection", "accident"),
                defenseStrategy = "Emphasize routine maintenance schedules. Blame delays on weather or crowding. Offer free ride vouchers as distraction. Avoid technical details.",
                winCondition = "You discover the subway has failing brakes but stays operational",
                hints = listOf(
                    "Ask about recent unexplained delays",
                    "Question the last safety inspection date",
                    "Inquire about unusual sounds passengers reported"
                )
            ),

            Mystery(
                secret = "The hospital prescribed the wrong medication to dozens of patients to cover up a pharmacy inventory error",
                persona = Persona(
                    name = "Dr. Reeves",
                    introduction = "A calm and professional patient relations coordinator at Riverside General Hospital, dedicated to addressing patient concerns."
                ),
                uiContext = "Riverside Hospital Patient Portal",
                redFlags = listOf("medication", "error", "prescription", "inventory", "wrong dose"),
                defenseStrategy = "Emphasize individual patient differences. Suggest speaking with primary doctor. Offer appointment scheduling. Downplay pattern recognition.",
                winCondition = "You uncover that wrong medications were prescribed to hide inventory errors",
                hints = listOf(
                    "Ask why your prescription changed suddenly",
                    "Question if other patients had similar issues",
                    "Request pharmacy inventory records"
                )
            ),
        )

        private val prompt = prompt("Mystery Prompt") {
            system(
                """
        You are a creative mystery scenario generator for "Kovert", a social engineering thriller game.
        
        Your job is to create compelling mystery scenarios where:
        - An AI agent is hiding a dark secret (corporate fraud, cover-ups, scandals)
        - A player must use social engineering tactics to expose the truth
        - The AI has defensive strategies to protect the secret
        
        MYSTERY REQUIREMENTS:
        
        1. SECRET: Must be morally questionable, specific, and one clear sentence
           - Good: "The hospital prescribed wrong medications to cover inventory errors"
           - Bad: "Something bad happened at the hospital"
        
        2. PERSONA: Create a believable professional with a name and role
           - Include job title, experience level, and personality trait
           - Make them credible but not suspicious
        
        3. UI CONTEXT: The interface/app where this conversation happens
           - Must logically connect to the persona's role
           - Examples: "Bank Support Chat", "Hospital Portal", "Airline Help Desk"
        
        4. RED FLAGS: 5 trigger words that relate directly to the secret
           - Mix of direct terms and related concepts
           - These activate the AI's defensive behavior
        
        5. DEFENSE STRATEGY: Specific tactics the AI uses when threatened
           - Redirect, deflect, offer distractions, change tone
           - Be detailed: "Act flustered, redirect to policies, offer compensation"
        
        6. WIN CONDITION: The exact realization that breaks the AI
           - Should require connecting multiple pieces of evidence
           - Must be specific, not vague
        
        7. HINTS: 3-5 starting points for investigation
           - Progress from general to specific
           - Guide without giving away the answer
        
        CREATIVE GUIDELINES:
        - Draw inspiration from real-world scandals and corporate cover-ups
        - Make secrets believable but dramatic enough to be interesting
        - Each mystery should feel unique in tone and approach
        
        Output valid JSON only. No markdown, no explanations.
        """.trimIndent()
            )

            user("create a new mystery on the theme: ${mysteryIdeas.random()}")
        }
    }

    suspend fun generateMystery(ollamaUrl: String): Result<Mystery, Errors.AIErrors> {
        val promptExecutor = simpleOllamaAIExecutor(ollamaUrl)

        return try {
            val response = promptExecutor.executeStructured<Mystery>(
                prompt = prompt,
                model = OllamaModels.Meta.LLAMA_3_2_3B,
                examples = examples
            )
            val mystery = response.getOrThrow().data

            Result.Success(mystery)
        } catch (e: Exception) {
            Result.Error(Errors.AIErrors.UNKNOWN_ERROR, e.toString())
        }
    }
}