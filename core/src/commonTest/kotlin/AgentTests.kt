import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import shub39.kovert.core.data.agents.ChatAgentHandler
import shub39.kovert.core.data.agents.MysteryFactory
import shub39.kovert.core.domain.MysteryData
import shub39.kovert.core.domain.Result
import kotlin.test.BeforeTest
import kotlin.test.Test

class AgentTests {
    private val mysteryFactory = MysteryFactory(MockRepo())
    private val chatAgentTools = MockChatAgentTools()
    private val chatAgentHandler = ChatAgentHandler(chatAgentTools)
    private val ollamaUrl = "http://192.168.31.67:11434"

    private var mysteryData: MysteryData? = null

    private fun testIn(title: String, block: suspend CoroutineScope.() -> Unit) = runBlocking {
        println("\n-- $title --")
        block.invoke(this)
        println("\n")
    }

    @BeforeTest
    fun setUpMystery() {
        runBlocking {
            val mystery = mysteryFactory.generateMystery(ollamaUrl)
            if (mystery is Result.Success) {
                mysteryData = mystery.data
                println(mysteryData)
            } else throw Exception("Could not generate mystery")
        }
    }

    @Test
    fun testChatAgentHints() = testIn("Testing Chat Agent Hints") {
        assert(mysteryData != null)

        chatAgentHandler.createChatAgent(
            ollamaUrl = ollamaUrl,
            mystery = mysteryData!!.mystery
        )

        mysteryData!!.mystery.hints.forEach {
            println(chatAgentHandler.chatAgent?.createAgentAndRun(it))
        }

        chatAgentHandler.chatAgent?.closeAll()
    }

    @Test
    fun testChatAgentRedFlags() = testIn("Testing Chat Agent Red Flags") {
        assert(mysteryData != null)

        chatAgentHandler.createChatAgent(
            ollamaUrl = ollamaUrl,
            mystery = mysteryData!!.mystery,
        )

        mysteryData!!.mystery.redFlags.forEach {
            println(chatAgentHandler.chatAgent?.createAgentAndRun(it))
        }

        chatAgentHandler.chatAgent?.closeAll()
    }
}