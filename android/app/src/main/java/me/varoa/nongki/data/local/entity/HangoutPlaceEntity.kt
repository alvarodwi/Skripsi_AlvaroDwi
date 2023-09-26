package me.varoa.nongki.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.varoa.nongki.domain.model.HangoutPlace

@Entity(tableName = "hangout_places")
data class HangoutPlaceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val address: String,
    val price: Int,
    val facility: Int,
    val location: Int,
    val reputation: Int,
    val lat: Double,
    val lng: Double,
)

fun HangoutPlace.asEntity() =
    HangoutPlaceEntity(
        id = id,
        name = name,
        address = address,
        price = price,
        facility = facility,
        location = location,
        reputation = reputation,
        lat = lat,
        lng = lng,
    )

fun HangoutPlaceEntity.asModel() =
    HangoutPlace(
        id = id,
        name = name,
        address = address,
        price = price,
        facility = facility,
        location = location,
        reputation = reputation,
        lat = lat,
        lng = lng,
    )
