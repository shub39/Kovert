package shub39.kovert.core.domain

import kotlinx.serialization.Serializable

@Serializable
data class Mystery(
    val secret: String,
    val persona: Persona,
    val uiContext: String,
    val redFlags: List<String>,
    val defenseStrategy: String,
    val winCondition: String,
    val hints: String
)

@Serializable
data class Persona(
    val name: String,
    val front: String
)