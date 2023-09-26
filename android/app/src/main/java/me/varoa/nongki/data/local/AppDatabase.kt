package me.varoa.nongki.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.varoa.nongki.data.local.dao.HangoutDao
import me.varoa.nongki.data.local.dao.SearchDao
import me.varoa.nongki.data.local.entity.HangoutPlaceEntity
import me.varoa.nongki.data.local.entity.SearchItemEntity

@Database(version = 1, entities = [HangoutPlaceEntity::class, SearchItemEntity::class])
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val searchDao: SearchDao
    abstract val hangoutDao: HangoutDao
}
