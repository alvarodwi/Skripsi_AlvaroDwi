package me.varoa.nongki.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.varoa.nongki.domain.model.HangoutPlace

interface DatasetRepository {
    fun getHangoutPlaces(q: String): Flow<PagingData<HangoutPlace>>

    suspend fun insertHangoutPlace(vararg places: HangoutPlace)

    suspend fun deleteAllHangoutPlaces()
}
