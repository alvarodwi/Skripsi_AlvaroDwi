package me.varoa.nongki.data.prefs

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PrefKeys {
    val FIRST_TIME_SYNC = booleanPreferencesKey("first_time_sync")
    val DATASET_SIZE = intPreferencesKey("dataset_size")
    val LAST_SYNC = stringPreferencesKey("last_sync")
    val FIRST_TIME_SEARCH = booleanPreferencesKey("first_time_search")
}
