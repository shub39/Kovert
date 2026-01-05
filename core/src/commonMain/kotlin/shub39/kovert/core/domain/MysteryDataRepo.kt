package shub39.kovert.core.domain

import kotlinx.coroutines.flow.Flow

interface MysteryDataRepo {
    suspend fun getMysteryData(): Flow<List<MysteryData>>
    suspend fun upsertMysteryData(mysteryData: MysteryData)
    suspend fun deleteMysteryData(mysteryData: MysteryData)
}