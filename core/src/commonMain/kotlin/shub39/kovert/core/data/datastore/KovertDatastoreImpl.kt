package shub39.kovert.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import shub39.kovert.core.domain.KovertDatastore

class KovertDatastoreImpl(
    private val datastore: DataStore<Preferences>
) : KovertDatastore {
    companion object {
        private val ollamaUrlKey = stringPreferencesKey("ollamaUrl")
    }

    override fun getOllamaUrl(): Flow<String> = datastore.data
        .map { it[ollamaUrlKey] ?: "" }
    override suspend fun setOllamaUrl(url: String) {
        datastore.edit {
            it[ollamaUrlKey] = url
        }
    }

}