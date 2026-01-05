package shub39.kovert.core.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MysteryDataDao {
    @Upsert
    suspend fun upsertMysteryData(mysteryData: MysteryDataEntity)

    @Query("SELECT * FROM mystery_data WHERE id = :id")
    suspend fun getMysteryDataById(id: Long): MysteryDataEntity?

    @Query("SELECT * FROM mystery_data")
    fun getMysteryData(): Flow<List<MysteryDataEntity>>
}