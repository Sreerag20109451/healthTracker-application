package org.agileSoftDev.utills.healthIndicators

class HealthIndexes {


    fun getBMI(height: Int, weight: Int): Double {

        var doubleHeight = height.toDouble()
        var heightInMeters = doubleHeight / 100
        var doubleWeight = weight.toDouble()
        var bmiValue = doubleWeight / (heightInMeters * heightInMeters)
        return bmiValue
    }


}