package me.varoa.nongki.ui.screen.search

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import logcat.logcat
import me.varoa.nongki.domain.model.SearchItem
import me.varoa.nongki.domain.repository.SearchRepository
import me.varoa.nongki.ui.base.BaseViewModel
import me.varoa.nongki.ui.base.OneTimeEvent
import java.time.LocalDateTime

class SearchViewModel(
    private val repository: SearchRepository,
) : BaseViewModel() {
    data class SearchFormData(
        val userLat: Double = 0.0,
        val userLng: Double = 0.0,
        val price: Int = 3,
        val location: Int = 3,
        val facility: Int = 3,
        val reputation: Int = 3,
    )

    data class NavigateToResult(val searchId: Int) : OneTimeEvent()

    private val mFormData = MutableStateFlow(SearchFormData())
    val formData get() = mFormData.asStateFlow()

    private val mIsLoading = MutableStateFlow(false)
    val isLoading get() = mIsLoading.asStateFlow()

    fun updateFormData(formData: SearchFormData) {
        logcat { "updateFormData($formData)" }
        viewModelScope.launch {
            mFormData.update { formData }
        }
    }

    fun onSearch() {
        val formData = mFormData.value
        val searchItem =
            SearchItem(
                timestamp = LocalDateTime.now().toString(),
                userLat = formData.userLat,
                userLng = formData.userLng,
                criteria =
                    listOf(
                        formData.price,
                        formData.facility,
                        formData.location,
                        formData.reputation,
                    ),
            )
        viewModelScope.launch {
            mIsLoading.update { true }
            repository.initiateSearch(searchItem)
                .catch { showErrorMessage(it.message) }
                .collect { result ->
                    if (result.isSuccess) {
                        sendNewEvent(NavigateToResult(result.getOrThrow()))
                    } else {
                        showErrorMessage(result.exceptionOrNull()?.message)
                    }
                }
        }
    }
}
