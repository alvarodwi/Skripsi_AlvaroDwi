package me.varoa.nongki.ui.screen.search.check

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.varoa.nongki.data.prefs.DataStoreManager
import me.varoa.nongki.ui.base.BaseViewModel

class SearchLocationCheckViewModel(
    private val prefs: DataStoreManager,
) : BaseViewModel() {
    fun finishLocationChecking() {
        viewModelScope.launch {
            prefs.finishLocationChecking()
        }
    }
}
