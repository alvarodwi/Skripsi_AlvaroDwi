package me.varoa.nongki.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import me.varoa.nongki.data.local.entity.HangoutPlaceEntity

@Dao
interface HangoutDao {
    @Query("select * from hangout_places where name like :q")
    fun getPlaces(q: String): PagingSource<Int, HangoutPlaceEntity>

    @Query("select * from hangout_places where id = :id")
    fun getPlace(id: Int): Flow<HangoutPlaceEntity>

    @Upsert
    fun upsert(vararg places: HangoutPlaceEntity)

    @Query("delete from hangout_places")
    fun deleteAll()
}
