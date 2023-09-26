package me.varoa.nongki.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

private val Context.dataStore by preferencesDataStore("prefs")

class DataStoreManager(
    appContext: Context,
) {
    private val prefsDataStore = appContext.dataStore

    val firstTimeSync
        get() =
            prefsDataStore.data.map { prefs ->
                prefs[PrefKeys.FIRST_TIME_SYNC] ?: true
            }

    val lastSync
        get() =
            prefsDataStore.data.map { prefs ->
                prefs[PrefKeys.LAST_SYNC] ?: ""
            }

    val datasetSize
        get() =
            prefsDataStore.data.map { prefs ->
                prefs[PrefKeys.DATASET_SIZE] ?: 0
            }

    suspend fun finishSync(
        datasetSize: Int,
        isFirstTime: Boolean = false,
    ) {
        prefsDataStore.edit { prefs ->
            prefs[PrefKeys.FIRST_TIME_SYNC] = isFirstTime
            prefs[PrefKeys.DATASET_SIZE] = datasetSize
            prefs[PrefKeys.LAST_SYNC] = LocalDateTime.now().toString()
        }
    }
}
