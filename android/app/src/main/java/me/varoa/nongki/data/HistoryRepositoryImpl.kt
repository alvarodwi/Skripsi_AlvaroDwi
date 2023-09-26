package me.varoa.nongki.data

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.varoa.nongki.data.local.dao.SearchDao
import me.varoa.nongki.domain.model.SearchItem
import me.varoa.nongki.domain.repository.HistoryRepository

class HistoryRepositoryImpl(
    private val dao: SearchDao,
) : HistoryRepository {
    override fun getHistories(): Flow<PagingData<SearchItem>> =
        flow {
        }

    override fun getHistory(id: Int): Flow<SearchItem> =
        flow {
        }

    override suspend fun deleteHistory(id: Int) {
    }
}
