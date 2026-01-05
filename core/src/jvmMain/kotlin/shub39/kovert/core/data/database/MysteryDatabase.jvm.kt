package shub39.kovert.core.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<MysteryDatabase> {
        val os = System.getProperty("os.name").lowercase()
        val useHome = System.getProperty("user.home")
        val appDataDir = when {
            os.contains("win") -> File(System.getenv("APPDATA"), "Kovert")
            os.contains("mac") -> File(useHome, "Library/Application Support/Kovert")
            else -> File(useHome, ".local/share/Kovert")
        }
        if (!appDataDir.exists()) {
            appDataDir.mkdirs()
        }
        val dbFile = File(appDataDir, MysteryDatabase.DB_NAME)

        return Room.databaseBuilder(dbFile.absolutePath)
    }
}