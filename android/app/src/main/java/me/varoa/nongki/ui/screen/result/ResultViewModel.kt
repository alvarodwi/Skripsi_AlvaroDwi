package me.varoa.nongki.ui.screen.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import me.varoa.nongki.domain.repository.SearchRepository

class ResultViewModel(
    repository: SearchRepository,
    handle: SavedStateHandle,
) : ViewModel() {
    private val id = handle.get<Int>("searchId") ?: 0

    val result = repository.getResult(id)
}
