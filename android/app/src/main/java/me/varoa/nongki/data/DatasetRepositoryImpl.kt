package me.varoa.nongki.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.varoa.nongki.data.local.dao.HangoutDao
import me.varoa.nongki.data.local.entity.HangoutPlaceEntity
import me.varoa.nongki.data.local.entity.asEntity
import me.varoa.nongki.data.local.entity.asModel
import me.varoa.nongki.domain.model.HangoutPlace
import me.varoa.nongki.domain.repository.DatasetRepository

class DatasetRepositoryImpl(
    private val dao: HangoutDao,
) : DatasetRepository {
    override fun getHangoutPlaces(q: String): Flow<PagingData<HangoutPlace>> =
        Pager(PagingConfig(pageSize = 20)) {
            dao.getPlaces(q)
        }.flow.map { pagingData -> pagingData.map(HangoutPlaceEntity::asModel) }

    override fun getHangoutPlace(id: Int): Flow<HangoutPlace> = dao.getPlace(id).map(HangoutPlaceEntity::asModel)

    override suspend fun insertHangoutPlace(vararg places: HangoutPlace) = dao.upsert(*places.map { it.asEntity() }.toTypedArray())

    override suspend fun deleteAllHangoutPlaces() = dao.deleteAll()
}
