package me.varoa.nongki.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import me.varoa.nongki.ui.base.OneTimeEvent.ShowErrorMessage

abstract class BaseViewModel : ViewModel() {
    private val eventChannel = Channel<OneTimeEvent>(Channel.BUFFERED)
    val events: Flow<OneTimeEvent> = eventChannel.receiveAsFlow()

    protected suspend fun sendNewEvent(event: OneTimeEvent) {
        eventChannel.send(event)
    }

    protected suspend fun showErrorMessage(message: String?) {
        eventChannel.send(ShowErrorMessage(message ?: ""))
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
