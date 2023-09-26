package me.varoa.nongki.data

import kotlinx.coroutines.flow.Flow
import me.varoa.nongki.data.local.dao.SearchDao
import me.varoa.nongki.domain.model.SearchItem
import me.varoa.nongki.domain.repository.SearchRepository

class SearchRepositoryImpl(
    private val dao: SearchDao,
) : SearchRepository {
    override fun getResult(id: Int): Flow<SearchItem> {
        TODO("Not yet implemented")
    }

    override suspend fun initiateSearch(data: SearchItem) {
        TODO("Not yet implemented")
    }
}
