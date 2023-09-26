package me.varoa.nongki.domain.model

data class HangoutPlace(
    val id: Int,
    val name: String,
    val address: String,
    val price: Int,
    val facility: Int,
    val location: Int,
    val reputation: Int,
    val lat: Double,
    val lng: Double,
)
