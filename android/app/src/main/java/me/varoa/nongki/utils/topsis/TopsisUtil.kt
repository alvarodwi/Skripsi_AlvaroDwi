package me.varoa.nongki.utils.topsis

import me.varoa.nongki.domain.model.HangoutPlace
import kotlin.math.pow
import kotlin.math.sqrt

object TopsisUtil {

    data class TopsisPlace(
        val id: Int,
        val criteria: List<Double>,
    )

    private fun HangoutPlace.toTopsisPlace() =
        TopsisPlace(
            id = this.id,
            criteria = listOf(
                this.price.toDouble(),
                this.facility.toDouble(),
                this.location.toDouble(),
                this.reputation.toDouble(),
            )
        )

    fun recommendPlaceFromCriteria(
        places: List<HangoutPlace>, // alternative
        weight: List<Int>, //user inputted criteria
    ): List<Int> {
        // convert place to TopsisPlace (for easier calculation)
        val dataset = places.map { it.toTopsisPlace() }
        println("original\n->${dataset.map { weight }}")

        // normalization + weighed
        val normalized = mutableListOf<TopsisPlace>()
        val factors = mutableListOf<Double>()
        for (i in weight.indices) {
            // factor
            var temp = 0.0
            for (data in dataset) {
                temp += data.criteria[i].pow(2)
            }
            factors.add(sqrt(temp))
        }
        println("factors\n-> ${factors}")

        for (data in dataset) {
            val newCriteria = data.criteria.mapIndexed { i, d ->
                (d / factors[i]) * weight[i]
            }
            val newData = data.copy(
                criteria = newCriteria
            )
            normalized.add(newData)
        }
        println("normalized\n-> ${normalized.map { it.criteria }}")

        val idealBest = (weight.indices).map { index ->
            normalized.mapNotNull { it.criteria.getOrNull(index) }.maxOrNull() ?: 0.0
        }
        val idealWorst = (weight.indices).map { index ->
            normalized.mapNotNull { it.criteria.getOrNull(index) }.minOrNull() ?: 0.0
        }
        println("idealBest\n-> ${idealBest}")
        println("idealWorst\n-> ${idealWorst}")

        // result is map of HangoutPlace id as key and topsis score as value
        val result: MutableMap<Int, Double> = mutableMapOf()
        for (data in normalized) {
            // positive distance
            var tempPos = 0.0
            for (i in weight.indices) {
                tempPos += (idealBest[i] - data.criteria[i]).pow(2)
            }
            val dPos = sqrt(tempPos)

            // negative distance
            var tempNeg = 0.0
            for (i in weight.indices) {
                tempNeg += (idealWorst[i] - data.criteria[i]).pow(2)
            }
            val dNeg = sqrt(tempNeg)
            println("distances\n-> $dPos and $dNeg")

            // topsis score, append to result
            val score = dNeg / (dPos + dNeg)
            result[data.id] = score
        }
        println("result->")

        // return place id ranked by topsis score
        val rankedPlace = places.sortedByDescending { result[it.id] }.take(5)
        rankedPlace.forEach {
            println("${it.name} -> ${result[it.id]}")
        }
        return rankedPlace.map { it.id }
    }
}
