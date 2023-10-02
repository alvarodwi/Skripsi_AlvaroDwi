package me.varoa.nongki.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import logcat.logcat
import me.varoa.nongki.data.local.dao.SearchDao
import me.varoa.nongki.data.local.entity.HangoutPlaceEntity
import me.varoa.nongki.data.local.entity.asEntity
import me.varoa.nongki.data.local.entity.asModel
import me.varoa.nongki.domain.model.SearchItem
import me.varoa.nongki.domain.repository.SearchRepository
import me.varoa.nongki.utils.topsis.TopsisUtil

class SearchRepositoryImpl(
    private val dao: SearchDao,
) : SearchRepository {
    override fun getResult(id: Int): Flow<SearchItem> =
        dao.getItem(id).map { entity ->
            val places = dao.getResultPlaces(entity.results).first()
            entity.asModel {
                places.map { it.asModel() }
            }
        }

    override suspend fun initiateSearch(data: SearchItem): Flow<Result<Int>> =
        flow {
            try {
                val dataset = dao.getDataset().first().map(HangoutPlaceEntity::asModel)
                val searchResult = TopsisUtil.recommendPlaceFromCriteria(places = dataset, criteria = data.criteria)
                val newData = data.asEntity().copy(results = searchResult)
                logcat { "initiateSearch.newData -> $newData" }
                val newItemId =
                    withContext(Dispatchers.IO) {
                        val rowId = dao.insert(newData)
                        dao.getItemIdByRowId(rowId).first()
                    }
                emit(Result.success(newItemId))
            } catch (ex: IllegalStateException) {
                ex.printStackTrace()
                emit(Result.failure(ex))
            }
        }
}
