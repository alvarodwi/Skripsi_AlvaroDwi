package me.varoa.nongki.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.varoa.nongki.domain.model.HangoutPlace
import me.varoa.nongki.domain.model.SearchItem

@Entity(tableName = "search_items")
data class SearchItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: String,
    val criteria: List<Int>,
    val userLat: Double,
    val userLng: Double,
    val results: List<Int>,
)

fun SearchItem.asEntity() =
    SearchItemEntity(
        id = id,
        timestamp = timestamp,
        criteria = criteria,
        userLat = userLat,
        userLng = userLng,
        results = resultIds,
    )

fun SearchItemEntity.asModel(getResultFromIds: () -> List<HangoutPlace>) =
    SearchItem(
        id = id,
        timestamp = timestamp,
        criteria = criteria,
        userLat = userLat,
        userLng = userLng,
        resultIds = results,
        results = getResultFromIds(),
    )
