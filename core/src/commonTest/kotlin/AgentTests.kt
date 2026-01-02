import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import shub39.kovert.core.data.agents.MysteryMakerAgentHandler
import kotlin.test.Test

class AgentTests {
    private val mysteryMakerAgentHandler = MysteryMakerAgentHandler()

    private fun testIn(title: String, block: suspend CoroutineScope.() -> Unit) = runBlocking {
        println("\n-- $title --")
        block.invoke(this)
        println("\n")
    }

    @Test
    fun testAgents() = testIn("Testing Agents") {
        mysteryMakerAgentHandler.generateNewMystery()
    }
}