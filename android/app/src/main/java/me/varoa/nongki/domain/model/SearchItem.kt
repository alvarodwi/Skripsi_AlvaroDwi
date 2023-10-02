package me.varoa.nongki.domain.model

data class SearchItem(
    val id: Int = 0,
    val timestamp: String,
    val criteria: List<Int>,
    val userLat: Double,
    val userLng: Double,
    val results: List<HangoutPlace> = emptyList(),
)
