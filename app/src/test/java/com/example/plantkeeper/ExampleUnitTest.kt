package com.example.plantkeeper

import com.example.plantkeeper.models.Plant
import com.example.plantkeeper.models.PlantSorter
import com.example.plantkeeper.models.PlantSorter.Companion.bubbleSort
import com.example.plantkeeper.models.PlantUpdate
import org.junit.Test

import org.junit.Assert.*


class ExampleUnitTest {

    ///Tests if plants are sorted correctly according to creationtime, the plant with the highest creationtime should be first.
    @Test
    fun plantsAreSorted() {
        var updatesList = listOf<PlantUpdate>()
        var plantOne = Plant("1", "1", 1, 1, 1, "1", 1.1, "", updatesList, 1)
        var plantTwo = Plant("1", "1", 1, 1, 1, "1", 1.1, "", updatesList, 2)
        var plantThree = Plant("1", "1", 1, 1, 1, "1", 1.1, "", updatesList, 3)
        var plantFour = Plant("1", "1", 1, 1, 1, "1", 1.1, "", updatesList, 4)

        var plantArray = arrayOf(plantTwo, plantFour, plantOne, plantThree)

        plantArray = plantArray.bubbleSort()

        assertEquals(plantFour.creationTime, plantArray[0].creationTime)
    }
}
