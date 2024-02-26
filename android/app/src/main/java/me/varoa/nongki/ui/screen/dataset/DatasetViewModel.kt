package me.varoa.nongki.ui.screen.dataset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import me.varoa.nongki.data.prefs.DataStoreManager
import me.varoa.nongki.domain.model.HangoutPlace
import me.varoa.nongki.domain.repository.DatasetRepository

class DatasetViewModel(
    private val repository: DatasetRepository,
    prefs: DataStoreManager,
) : ViewModel() {
    val datasetSize = prefs.datasetSize

    private val mQuery: MutableStateFlow<String> = MutableStateFlow("")
    val query: StateFlow<String> = mQuery.asStateFlow()
    val places: Flow<PagingData<HangoutPlace>>
        get() = mQuery.flatMapLatest { q ->
            repository.getHangoutPlaces("%${q}%")
        }.cachedIn(viewModelScope)

    fun search(q: String) {
        viewModelScope.launch {
            mQuery.emit(q)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
