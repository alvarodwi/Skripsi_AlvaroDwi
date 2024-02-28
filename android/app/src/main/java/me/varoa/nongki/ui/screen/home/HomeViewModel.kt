package me.varoa.nongki.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.varoa.nongki.data.prefs.DataStoreManager

class HomeViewModel(
    private val prefs: DataStoreManager,
) : ViewModel() {
    val isFirstTimeSync = prefs.firstTimeSync
    val isFirstTimeSearch = prefs.isFirstTimeSearch

    fun finishSync(datasetSize: Int) {
        viewModelScope.launch {
            prefs.finishSync(datasetSize)
        }
    }
}
