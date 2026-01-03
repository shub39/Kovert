package shub39.kovert.core.domain

import kotlinx.coroutines.flow.Flow

interface KovertDatastore {
    fun getOllamaUrl(): Flow<String>
    suspend fun setOllamaUrl(url: String)
}