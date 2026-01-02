package shub39.kovert.core.domain

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val sender: Entity,
    val content: String,
    val isBlurred: Boolean = false
)