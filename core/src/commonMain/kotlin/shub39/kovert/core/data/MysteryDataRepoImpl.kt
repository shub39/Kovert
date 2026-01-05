package shub39.kovert.core.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import shub39.kovert.core.data.database.MysteryDataDao
import shub39.kovert.core.data.database.toMysteryData
import shub39.kovert.core.data.database.toMysteryEntity
import shub39.kovert.core.domain.MysteryData
import shub39.kovert.core.domain.MysteryDataRepo

class MysteryDataRepoImpl(
    val mysteryDataDao: MysteryDataDao
) : MysteryDataRepo {
    override suspend fun getMysteryData(): Flow<List<MysteryData>> {
        return mysteryDataDao
            .getMysteryData()
            .map { it.map { mysteryDataEntity -> mysteryDataEntity.toMysteryData() } }
    }

    override suspend fun upsertMysteryData(mysteryData: MysteryData) {
        mysteryDataDao.upsertMysteryData(mysteryData.toMysteryEntity())
    }

    override suspend fun deleteMysteryData(mysteryData: MysteryData) {
        mysteryDataDao.deleteMysteryData(mysteryData.toMysteryEntity())
    }
}