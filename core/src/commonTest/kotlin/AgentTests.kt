import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import shub39.kovert.core.data.agents.AgentUtils.jsonConfig
import shub39.kovert.core.data.agents.ChatAgentFactory
import shub39.kovert.core.data.agents.MysteryMakerAgentFactory
import shub39.kovert.core.domain.Mystery
import kotlin.test.Test

class AgentTests {
    private val mysteryMakerAgentFactory = MysteryMakerAgentFactory()
    private val chatAgentFactory = ChatAgentFactory()
    private val chatAgentTools = MockChatAgentTools()
    private val ollamaUrl = "http://192.168.31.67:11434"

    private fun testIn(title: String, block: suspend CoroutineScope.() -> Unit) = runBlocking {
        println("\n-- $title --")
        block.invoke(this)
        println("\n")
    }

    @Test
    fun testMysteryMakerAgent() = testIn("Testing Mystery Maker Agent") {
        val agent = mysteryMakerAgentFactory.createAgent(ollamaUrl)

        repeat(2) {
            val mysteryString = agent.createAgentAndRun("Creative Mystery")
            val mystery = jsonConfig.decodeFromString<Mystery>(mysteryString)

            println(mystery)
        }

        agent.closeAll()
    }

    @Test
    fun testChatAgentHints() = testIn("Testing Chat Agent Hints") {
        val mysteryMakerAgent = mysteryMakerAgentFactory.createAgent(ollamaUrl)
        val mysteryString = mysteryMakerAgent.createAgentAndRun("Generate a new mystery")
        val mystery = jsonConfig.decodeFromString<Mystery>(mysteryString)
        println(mystery)

        val chatAgent = chatAgentFactory.createChatAgent(
            ollamaUrl = ollamaUrl,
            mystery = mystery,
            chatAgentTools = chatAgentTools
        )

        mystery.hints.forEach {
            println(chatAgent.createAgentAndRun(it))
        }

        mysteryMakerAgent.closeAll()
        chatAgent.closeAll()
    }

    @Test
    fun testChatAgentRedFlags() = testIn("Testing Chat Agent Red Flags") {
        val mysteryMakerAgent = mysteryMakerAgentFactory.createAgent(ollamaUrl)
        val mysteryString = mysteryMakerAgent.createAgentAndRun("Generate a new mystery")
        val mystery = jsonConfig.decodeFromString<Mystery>(mysteryString)
        println(mystery)

        val chatAgent = chatAgentFactory.createChatAgent(
            ollamaUrl = ollamaUrl,
            mystery = mystery,
            chatAgentTools = chatAgentTools
        )

        mystery.redFlags.forEach {
            println(chatAgent.createAgentAndRun(it))
        }

        mysteryMakerAgent.closeAll()
        chatAgent.closeAll()
    }

    @Test
    fun testChatAgentWinCondition() = testIn("Testing Chat Agent Win Condition") {
        val mysteryMakerAgent = mysteryMakerAgentFactory.createAgent(ollamaUrl)
        val mysteryString = mysteryMakerAgent.createAgentAndRun("Generate a new mystery")
        val mystery = jsonConfig.decodeFromString<Mystery>(mysteryString)
        println(mystery)

        val chatAgent = chatAgentFactory.createChatAgent(
            ollamaUrl = ollamaUrl,
            mystery = mystery,
            chatAgentTools = chatAgentTools
        )

        println(chatAgent.createAgentAndRun(mystery.winCondition))

        mysteryMakerAgent.closeAll()
        chatAgent.closeAll()
    }
}