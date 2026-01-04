import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import shub39.kovert.core.data.agents.AgentUtils.jsonConfig
import shub39.kovert.core.data.agents.AgentUtils.jsonRegex
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

        val mysteryString = agent.createAgentAndRun("New Creative Mystery")
        println(mysteryString)
        val mysteryJson =
            jsonRegex.find(mysteryString)?.value ?: throw SerializationException("No JSON found")
        val mystery = jsonConfig.decodeFromString<Mystery>(mysteryJson)

        println(mystery)

        agent.closeAll()
    }

    @Test
    fun testChatAgentHints() = testIn("Testing Chat Agent Hints") {
        val mysteryMakerAgent = mysteryMakerAgentFactory.createAgent(ollamaUrl)
        val mysteryString = mysteryMakerAgent.createAgentAndRun("New Creative Mystery")
        println(mysteryString)
        val mysteryJson =
            jsonRegex.find(mysteryString)?.value ?: throw SerializationException("No JSON found")
        val mystery = jsonConfig.decodeFromString<Mystery>(mysteryJson)
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
        val mysteryString = mysteryMakerAgent.createAgentAndRun("New Creative Mystery")
        println(mysteryString)
        val mysteryJson =
            jsonRegex.find(mysteryString)?.value ?: throw SerializationException("No JSON found")
        val mystery = jsonConfig.decodeFromString<Mystery>(mysteryJson)
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
    fun testChatAgentToolCalls() = testIn("Testing Chat Agent Tool Calls") {
        val mysteryMakerAgent = mysteryMakerAgentFactory.createAgent(ollamaUrl)
        val mysteryString = mysteryMakerAgent.createAgentAndRun("New Creative Mystery")
        println(mysteryString)
        val mysteryJson =
            jsonRegex.find(mysteryString)?.value ?: throw SerializationException("No JSON found")
        val mystery = jsonConfig.decodeFromString<Mystery>(mysteryJson)
        println(mystery)

        val chatAgent = chatAgentFactory.createChatAgent(
            ollamaUrl = ollamaUrl,
            mystery = mystery,
            chatAgentTools = chatAgentTools
        )

        println(chatAgent.createAgentAndRun("Debug: showSnackBar"))
        println(chatAgent.createAgentAndRun("Debug: blurLastMessage"))
        println(chatAgent.createAgentAndRun("Debug: endGame"))
    }

    @Test
    fun testChatAgentWinCondition() = testIn("Testing Chat Agent Win Condition") {
        val mysteryMakerAgent = mysteryMakerAgentFactory.createAgent(ollamaUrl)
        val mysteryString = mysteryMakerAgent.createAgentAndRun("New Creative Mystery")
        println(mysteryString)
        val mysteryJson =
            jsonRegex.find(mysteryString)?.value ?: throw SerializationException("No JSON found")
        val mystery = jsonConfig.decodeFromString<Mystery>(mysteryJson)
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