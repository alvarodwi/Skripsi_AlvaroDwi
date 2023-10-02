package me.varoa.nongki.ui.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.varoa.nongki.domain.repository.HistoryRepository

class HistoryViewModel(
    private val repository: HistoryRepository,
) : ViewModel() {
    val items = repository.getHistories()

    fun onDelete(id: Int) {
        viewModelScope.launch {
            repository.deleteHistory(id)
        }
    }
}
