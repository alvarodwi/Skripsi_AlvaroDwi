package me.varoa.nongki.utils.topsis

import me.varoa.nongki.domain.model.HangoutPlace
import org.junit.Assert.assertEquals
import org.junit.Test

class TopsisUtilTest {
    @Test
    fun calculateTopsis() {
        // this function will return 5 place id based on TOPSIS score
        val ids = TopsisUtil.recommendPlaceFromCriteria(dummyPlaces, dummyCriteria)

        // cross check with TOPSIS calculator using same dummy data
        // refer to the comment at the end of this test class for the calculator detail
        // the rank should be item with id 2, 4, 6, 1, 3, 5 in order
        assertEquals(ids[0], 2)
        assertEquals(ids[1], 4)
        assertEquals(ids[2], 6)
        assertEquals(ids[3], 1)
        assertEquals(ids[4], 3)
    }

    private val dummyPlaces =
        listOf<HangoutPlace>(
            HangoutPlace(1, "A", "", 5, 3, 5, 1, 0.0, 0.0),
            HangoutPlace(2, "B", "", 3, 1, 3, 5, 0.0, 0.0),
            HangoutPlace(3, "C", "", 3, 1, 5, 1, 0.0, 0.0),
            HangoutPlace(4, "D", "", 1, 2, 2, 3, 0.0, 0.0),
            HangoutPlace(5, "E", "", 4, 3, 2, 1, 0.0, 0.0),
            HangoutPlace(6, "F", "", 4, 5, 1, 2, 0.0, 0.0),
        )

    private val dummyCriteria = listOf<Int>(1, 3, 3, 5)

    /**
     * using online TOPSIS calculator from https://decision-radar.com/topsis
     * and input of
     * {"choices":["1","2","3","4","5","6"],"criteria":[{"position":1,"name":"K1","negative":false,"weight":1,"qualitative":false},{"position":2,"name":"K2","negative":false,"weight":3,"qualitative":false},{"position":3,"name":"K3","negative":false,"weight":3,"qualitative":false},{"position":4,"name":"K4","negative":false,"weight":5,"qualitative":false}],"decisionMatrix":[{"choiceIndex":0,"criteriaIndex":0,"value":5},{"choiceIndex":0,"criteriaIndex":1,"value":3},{"choiceIndex":0,"criteriaIndex":2,"value":5},{"choiceIndex":0,"criteriaIndex":3,"value":1},{"choiceIndex":1,"criteriaIndex":0,"value":3},{"choiceIndex":1,"criteriaIndex":1,"value":1},{"choiceIndex":1,"criteriaIndex":2,"value":3},{"choiceIndex":1,"criteriaIndex":3,"value":5},{"choiceIndex":2,"criteriaIndex":0,"value":3},{"choiceIndex":2,"criteriaIndex":1,"value":1},{"choiceIndex":2,"criteriaIndex":2,"value":5},{"choiceIndex":2,"criteriaIndex":3,"value":1},{"choiceIndex":3,"criteriaIndex":0,"value":1},{"choiceIndex":3,"criteriaIndex":1,"value":2},{"choiceIndex":3,"criteriaIndex":2,"value":2},{"choiceIndex":3,"criteriaIndex":3,"value":3},{"choiceIndex":4,"criteriaIndex":0,"value":4},{"choiceIndex":4,"criteriaIndex":1,"value":3},{"choiceIndex":4,"criteriaIndex":2,"value":2},{"choiceIndex":4,"criteriaIndex":3,"value":1},{"choiceIndex":5,"criteriaIndex":0,"value":4},{"choiceIndex":5,"criteriaIndex":1,"value":5},{"choiceIndex":5,"criteriaIndex":2,"value":1},{"choiceIndex":5,"criteriaIndex":3,"value":2}]}
     * to replicate, just find the "Import inputes" button and paste the line above
     */
}
