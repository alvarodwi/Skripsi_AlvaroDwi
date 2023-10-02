package me.varoa.nongki.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.varoa.nongki.data.local.dao.SearchDao
import me.varoa.nongki.data.local.entity.asModel
import me.varoa.nongki.domain.model.SearchItem
import me.varoa.nongki.domain.repository.HistoryRepository

class HistoryRepositoryImpl(
    private val dao: SearchDao,
) : HistoryRepository {
    override fun getHistories(): Flow<PagingData<SearchItem>> =
        Pager(PagingConfig(pageSize = 20)) {
            dao.getItems()
        }.flow.map { pagingData -> pagingData.map { it.asModel { emptyList() } } }

    override fun getHistory(id: Int): Flow<SearchItem> =
        dao.getItem(id).map { entity ->
            val places =
                withContext(Dispatchers.IO) {
                    dao.getResultPlaces(entity.results)
                }
            entity.asModel {
                places.map { it.asModel() }
            }
        }

    override suspend fun deleteHistory(id: Int) {
        withContext(Dispatchers.IO) {
            dao.delete(id)
        }
    }
}
