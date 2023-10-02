package me.varoa.nongki.utils.topsis

import me.varoa.nongki.domain.model.HangoutPlace

object TopsisUtil {
    fun recommendPlaceFromCriteria(
        places: List<HangoutPlace>,
        criteria: List<Int>,
    ): List<Int> {
        return listOf(0, 2, 3)
    }
}
