package com.example.plantkeeper.models

class PlantSorter {

    companion object {

        fun Array<Plant>.bubbleSort() : Array<Plant> {
            var swap = true
            while(swap) {
                swap = false
                for(i in 0 until this.size-1) {
                    if(this[i].creationTime < this[i+1].creationTime) {
                        val plant = this[i]
                        this[i] = this[i + 1]
                        this[i + 1] = plant

                        swap = true
                    }
                }
            }
            return this
        }
    }
}