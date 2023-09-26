package me.varoa.nongki.domain.model

data class SearchItem(
    val id: Int,
    val timestamp: String,
    val criteria: List<Int>,
    val results: List<HangoutPlace>,
)
