package shub39.kovert.core.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import shub39.kovert.core.domain.ChatMessage
import shub39.kovert.core.domain.Mystery
import shub39.kovert.core.domain.MysteryData

@Entity(tableName = "mystery_data")
data class MysteryDataEntity(
    @PrimaryKey val id: Long = 0,
    val mystery: Mystery,
    val chatMessages: List<ChatMessage>,
    val isSolved: Boolean
)

fun MysteryData.toMysteryEntity(): MysteryDataEntity {
    return MysteryDataEntity(
        id = id,
        mystery = mystery,
        chatMessages = chatMessages,
        isSolved = isSolved
    )
}

fun MysteryDataEntity.toMysteryData(): MysteryData {
    return MysteryData(
        id = id,
        mystery = mystery,
        chatMessages = chatMessages,
        isSolved = isSolved
    )
}