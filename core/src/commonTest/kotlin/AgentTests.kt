import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import shub39.kovert.core.data.agents.ChatAgentFactory
import shub39.kovert.core.data.agents.MysteryFactory
import shub39.kovert.core.domain.Result
import kotlin.test.Test

class AgentTests {
    private val mysteryFactory = MysteryFactory()
    private val chatAgentHandler = ChatAgentFactory()
    private val chatAgentTools = MockChatAgentTools()
    private val ollamaUrl = "http://192.168.31.67:11434"

    private fun testIn(title: String, block: suspend CoroutineScope.() -> Unit) = runBlocking {
        println("\n-- $title --")
        block.invoke(this)
        println("\n")
    }

    @Test
    fun testMysteryMakerAgent() = testIn("Testing Mystery Maker Agent") {
        println(mysteryFactory.generateMystery(ollamaUrl))
    }

    @Test
    fun testChatAgentHints() = testIn("Testing Chat Agent Hints") {
        val mysteryResponse = mysteryFactory.generateMystery(ollamaUrl)
        println(mysteryResponse)

        if (mysteryResponse is Result.Success) {
            val mystery = mysteryResponse.data

            val chatAgent = chatAgentHandler.createChatAgent(
                ollamaUrl = ollamaUrl,
                mystery = mystery,
                chatAgentTools = chatAgentTools
            )

            mystery.hints.forEach {
                println(chatAgent.createAgentAndRun(it))
            }

            chatAgent.closeAll()
        }
    }

    @Test
    fun testChatAgentRedFlags() = testIn("Testing Chat Agent Red Flags") {
        val mysteryResponse = mysteryFactory.generateMystery(ollamaUrl)
        println(mysteryResponse)

        if (mysteryResponse is Result.Success) {
            val mystery = mysteryResponse.data

            val chatAgent = chatAgentHandler.createChatAgent(
                ollamaUrl = ollamaUrl,
                mystery = mystery,
                chatAgentTools = chatAgentTools
            )

            mystery.redFlags.forEach {
                println(chatAgent.createAgentAndRun(it))
            }

            chatAgent.closeAll()
        }
    }

    @Test
    fun testChatAgentToolCalls() = testIn("Testing Chat Agent Tool Calls") {
        val mysteryResponse = mysteryFactory.generateMystery(ollamaUrl)
        println(mysteryResponse)

        if (mysteryResponse is Result.Success) {
            val mystery = mysteryResponse.data

            val chatAgent = chatAgentHandler.createChatAgent(
                ollamaUrl = ollamaUrl,
                mystery = mystery,
                chatAgentTools = chatAgentTools
            )

            println(chatAgent.createAgentAndRun("Debug: showSnackBar"))
            println(chatAgent.createAgentAndRun("Debug: blurLastMessage"))
            println(chatAgent.createAgentAndRun("Debug: endGame"))

            chatAgent.closeAll()
        }
    }

    @Test
    fun testChatAgentWinCondition() = testIn("Testing Chat Agent Win Condition") {
        val mysteryResponse = mysteryFactory.generateMystery(ollamaUrl)
        println(mysteryResponse)

        if (mysteryResponse is Result.Success) {
            val mystery = mysteryResponse.data

            val chatAgent = chatAgentHandler.createChatAgent(
                ollamaUrl = ollamaUrl,
                mystery = mystery,
                chatAgentTools = chatAgentTools
            )

            println(chatAgent.createAgentAndRun(mystery.winCondition))

            chatAgent.closeAll()
        }
    }
}