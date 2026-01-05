package shub39.kovert.core.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters

@Database(
    entities = [MysteryDataEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
@ConstructedBy(DbConstructor::class)
abstract class MysteryDatabase: RoomDatabase() {
    abstract val mysteryDataDao: MysteryDataDao

    companion object {
        const val DB_NAME = "mystery_data.db"
    }
}

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<MysteryDatabase>
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object DbConstructor: RoomDatabaseConstructor<MysteryDatabase> {
    override fun initialize(): MysteryDatabase
}