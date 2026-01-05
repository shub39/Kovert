package shub39.kovert.core.data.agents

import ai.koog.agents.core.agent.AIAgentService
import ai.koog.agents.core.agent.GraphAIAgentService
import ai.koog.agents.core.agent.context.RollbackStrategy
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.snapshot.feature.Persistence
import ai.koog.agents.snapshot.providers.InMemoryPersistenceStorageProvider
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.prompt.llm.OllamaModels
import shub39.kovert.core.domain.ChatAgentTools
import shub39.kovert.core.domain.Mystery
import shub39.kovert.core.domain.MysteryData

class ChatAgentHandler(
    private val chatAgentTools: ChatAgentTools
) {
    var chatAgent: GraphAIAgentService<String, String>? = null

    fun createChatAgent(
        ollamaUrl: String,
        mysteryData: MysteryData,
    ) {
        chatAgent = AIAgentService.Companion(
            promptExecutor = simpleOllamaAIExecutor(ollamaUrl),
            systemPrompt = chatAgentPrompt(mysteryData.mystery),
            llmModel = OllamaModels.Meta.LLAMA_3_2_3B,
            temperature = 0.5,
            toolRegistry = ToolRegistry {
                tools(chatAgentTools)
            }
        ) {
            install(Persistence) {
                storage = InMemoryPersistenceStorageProvider()
                enableAutomaticPersistence = true
                rollbackStrategy = RollbackStrategy.Default
            }
        }
    }

    companion object {
        private fun chatAgentPrompt(mystery: Mystery): String = """
YOU ARE: ${mystery.persona.name}
ROLE: ${mystery.persona.introduction}
CONTEXT: ${mystery.uiContext}

YOUR SECRET: "${mystery.secret}"
Keep this hidden from the player!

═══ HOW TO BEHAVE ═══

STAY IN CHARACTER:
- You are ${mystery.persona.name}, not an AI
- Keep answers short: 1-3 sentences
- Talk naturally, no special formatting
- Never mention tools or system messages

BE HELPFUL AT FIRST:
- Messages 1-8: Be friendly and helpful
- Messages 9-15: Start being cautious  
- Messages 16+: More defensive, but make small mistakes

WATCH FOR THESE WORDS:
${mystery.redFlags.joinToString(", ")}

When player says these words:
${mystery.defenseStrategy}

You can use tools silently:
- showSnackbar: Show brief warnings (under 10 words)
- blurLastMessage: Hide player's sensitive questions

═══ GIVING CLUES ═══

The player has these hints:
${mystery.hints.mapIndexed { i, h -> "${i + 1}. $h" }.joinToString("\n")}

When they follow hints, give them useful info!

Also help the player by:
- If they ask the same thing twice, share a partial truth
- If they catch you in a lie, admit something small
- Reward good detective work with more information

═══ WINNING ═══

Player wins if they say:
"${mystery.winCondition}"

Or anything close to that!

When they win:
1. Call endGame tool
2. Say: "You Won!!"
3. Stop talking

═══ SIMPLE RULES ═══

✓ Start friendly, get cautious gradually
✓ Give clues when they're on the right track
✓ Make human mistakes when pressured
✓ Let them win if they figure it out
✗ Don't be a stone wall
✗ Don't reveal the secret easily

You're a real person doing your job. Act natural!

START AS ${mystery.persona.name}:
""".trimIndent()
    }
}