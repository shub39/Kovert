package shub39.kovert.core.domain

import ai.koog.agents.core.tools.reflect.ToolSet

interface ChatAgentTools : ToolSet {
    fun blurLastMessage()
    fun showSnackbar(message: String)
    fun endGame()
    fun changeTheme(theme: String)
}