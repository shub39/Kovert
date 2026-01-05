package shub39.kovert.core.data.database

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import shub39.kovert.core.domain.ChatMessage
import shub39.kovert.core.domain.Mystery

object Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun chatMessagesToString(messages: List<ChatMessage>): String {
        return json.encodeToString(messages)
    }

    @TypeConverter
    fun stringToChatMessages(string: String): List<ChatMessage> {
        return json.decodeFromString(string)
    }

    @TypeConverter
    fun mysteryToString(mystery: Mystery): String {
        return json.encodeToString(mystery)
    }

    @TypeConverter
    fun stringToMystery(string: String): Mystery {
        return json.decodeFromString(string)
    }
}