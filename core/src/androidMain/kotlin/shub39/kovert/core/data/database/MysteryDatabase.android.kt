package shub39.kovert.core.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class DatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<MysteryDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(MysteryDatabase.DB_NAME)

        return Room.databaseBuilder(appContext, dbFile.absolutePath)
    }
}