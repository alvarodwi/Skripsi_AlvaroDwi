package me.varoa.nongki.domain.repository

import kotlinx.coroutines.flow.Flow
import me.varoa.nongki.domain.model.SearchItem

interface SearchRepository {
    fun getResult(id: Int): Flow<SearchItem>

    suspend fun initiateSearch(data: SearchItem)
}
