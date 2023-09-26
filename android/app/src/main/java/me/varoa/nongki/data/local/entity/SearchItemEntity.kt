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
    val results: List<Int>,
)

fun SearchItem.asEntity() =
    SearchItemEntity(
        id = id,
        timestamp = timestamp,
        criteria = criteria,
        results = results.map { it.id },
    )

fun SearchItemEntity.asModel(getResultFromIds: (List<Int>) -> List<HangoutPlace>) =
    SearchItem(
        id = id,
        timestamp = timestamp,
        criteria = criteria,
        results = getResultFromIds(results),
    )
