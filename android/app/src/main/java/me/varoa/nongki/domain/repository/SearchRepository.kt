package me.varoa.nongki.domain.repository

import kotlinx.coroutines.flow.Flow
import me.varoa.nongki.domain.model.SearchItem

interface SearchRepository {
    suspend fun initiateSearch(data: SearchItem): Flow<Result<Int>>

    fun getResult(id: Int): Flow<SearchItem>
}
