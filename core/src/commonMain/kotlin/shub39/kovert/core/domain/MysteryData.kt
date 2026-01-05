package shub39.kovert.core.domain

import kotlinx.serialization.Serializable

@Serializable
data class MysteryData(
    val id: Long,
    val mystery: Mystery,
    val chatMessages: List<ChatMessage>,
    val isSolved: Boolean
)