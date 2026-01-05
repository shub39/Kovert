import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import shub39.kovert.core.domain.MysteryData
import shub39.kovert.core.domain.MysteryDataRepo

class MockRepo: MysteryDataRepo {
    override suspend fun getMysteryData(): Flow<List<MysteryData>> = flowOf(emptyList())
    override suspend fun upsertMysteryData(mysteryData: MysteryData) {}
    override suspend fun deleteMysteryData(mysteryData: MysteryData) {}
}