package me.varoa.nongki.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.varoa.nongki.domain.model.SearchItem

interface HistoryRepository {
    fun getHistories(): Flow<PagingData<SearchItem>>
    suspend fun deleteHistory(id: Int)
}
