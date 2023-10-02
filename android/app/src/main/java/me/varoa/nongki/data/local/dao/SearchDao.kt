package me.varoa.nongki.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.varoa.nongki.data.local.entity.HangoutPlaceEntity
import me.varoa.nongki.data.local.entity.SearchItemEntity

@Dao
interface SearchDao {
    @Query("select * from hangout_places")
    fun getDataset(): Flow<List<HangoutPlaceEntity>>

    @Query("select * from search_items")
    fun getItems(): PagingSource<Int, SearchItemEntity>

    @Query("select * from search_items where id = :id")
    fun getItem(id: Int): Flow<SearchItemEntity>

    @Query("select * from hangout_places where id in (:ids)")
    fun getResultPlaces(ids: List<Int>): List<HangoutPlaceEntity>

    @Query("select id from search_items where rowid = :rowId")
    fun getItemIdByRowId(rowId: Long): Flow<Int>

    @Insert
    fun insert(item: SearchItemEntity): Long

    @Query("delete from search_items where id = :id")
    fun delete(id: Int)
}
