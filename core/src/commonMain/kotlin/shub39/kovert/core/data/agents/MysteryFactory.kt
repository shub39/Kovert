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

        Mystery(
            secret = "The principal has been altering student grades to improve the school's ranking and secure more funding",
            persona = Persona(
                name = "Ms. Rodriguez",
                introduction = "An upbeat administrative assistant at Lincoln High School who handles parent inquiries and student records."
            ),
            uiContext = "Lincoln High School Parent Portal",
            redFlags = listOf("grades", "changed", "ranking", "funding", "scores"),
            defenseStrategy = "Credit improved teaching methods. Praise student effort. Redirect to extracurricular achievements. Suggest speaking with individual teachers.",
            winCondition = "You realize grades were altered to boost school rankings",
            hints = listOf(
                "Ask why multiple students' grades suddenly improved",
                "Question the school's dramatic ranking jump",
                "Request original grade records from last semester"
            )
        ),

        Mystery(
            secret = "The factory has been dumping toxic waste into the river at night to avoid expensive disposal fees",
            persona = Persona(
                name = "Tom",
                introduction = "A helpful environmental compliance officer at GreenManufacturing Corp, always ready to discuss the company's sustainability efforts."
            ),
            uiContext = "GreenManufacturing Compliance Chat",
            redFlags = listOf("dumping", "waste", "river", "toxic", "night shift"),
            defenseStrategy = "Highlight environmental awards. Share recycling statistics. Blame upstream companies. Offer facility tour during daytime only.",
            winCondition = "You expose the illegal toxic waste dumping at night",
            hints = listOf(
                "Ask about waste disposal procedures",
                "Question recent water quality complaints downstream",
                "Inquire about night shift activities"
            )
        ),

        Mystery(
            secret = "The investment advisor has been using client funds to cover losses from his personal gambling debts",
            persona = Persona(
                name = "Richard",
                introduction = "A polished and reassuring financial advisor at Premier Wealth Management with a reputation for high returns."
            ),
            uiContext = "Premier Wealth Client Portal",
            redFlags = listOf("withdrawal", "transfer", "gambling", "losses", "personal"),
            defenseStrategy = "Blame market volatility. Show impressive (fake) portfolio charts. Discourage withdrawals with penalty warnings. Offer bonus investments.",
            winCondition = "You discover client funds were used for personal gambling debts",
            hints = listOf(
                "Ask why withdrawals are being delayed",
                "Question unexplained account transfers",
                "Request independent audit of your portfolio"
            )
        ),

        Mystery(
            secret = "The restaurant has been serving expired meat that was relabeled with false dates to reduce waste costs",
            persona = Persona(
                name = "Chef Maria",
                introduction = "A warm and passionate restaurant manager at Bella Vista Italian Restaurant, proud of her 15 years in the culinary industry."
            ),
            uiContext = "Bella Vista Reservation and Inquiry System",
            redFlags = listOf("expired", "date", "sick", "spoiled", "food poisoning"),
            defenseStrategy = "Emphasize fresh ingredients motto. Blame customer food allergies. Offer free meals. Redirect to positive reviews and health inspection scores.",
            winCondition = "You uncover that expired meat is being relabeled and served",
            hints = listOf(
                "Ask about recent food poisoning reports",
                "Question meat supplier and delivery schedules",
                "Inquire about the date labeling process"
            )
        ),

        Mystery(
            secret = "The property manager is showing the same apartment to multiple tenants and collecting deposits from all of them",
            persona = Persona(
                name = "Lisa",
                introduction = "An enthusiastic leasing agent at Skyline Properties, always eager to help people find their perfect home."
            ),
            uiContext = "Skyline Properties Rental Portal",
            redFlags = listOf("deposit", "multiple", "scam", "available", "refund"),
            defenseStrategy = "Create urgency about high demand. Show fake availability calendar. Promise refunds that never come. Blame system errors for double bookings.",
            winCondition = "You expose the duplicate deposit scam on the same apartment",
            hints = listOf(
                "Ask how many people viewed the apartment today",
                "Question why others mentioned making deposits",
                "Request proof the apartment is actually available"
            )
        )
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
        - Vary the industries: healthcare, finance, tech, government, retail, etc.
        - Make secrets believable but dramatic enough to be interesting
        - Each mystery should feel unique in tone and approach
        
        Output valid JSON only. No markdown, no explanations.
        """.trimIndent()
        )

        user("create a new mystery")
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