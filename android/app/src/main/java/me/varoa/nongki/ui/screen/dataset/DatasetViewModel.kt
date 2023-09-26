package me.varoa.nongki.ui.screen.dataset

import androidx.lifecycle.ViewModel
import me.varoa.nongki.data.prefs.DataStoreManager
import me.varoa.nongki.domain.repository.DatasetRepository

class DatasetViewModel(
    private val repository: DatasetRepository,
    private val prefs: DataStoreManager,
) : ViewModel() {
    val places = repository.getHangoutPlaces("%%")
    val datasetSize = prefs.datasetSize
}
