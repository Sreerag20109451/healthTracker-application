package org.agileSoftDev.utills.healthIndicators

class HealthIndexes {


    fun getBMI(height: Int, weight: Int): Map<Double, String> {

        var doubleHeight = height.toDouble()
        var heightInMeters = doubleHeight / 100
        var doubleWeight = weight.toDouble()
        var bmiValue = doubleWeight / (heightInMeters * heightInMeters)

        var bmi :MutableMap<Double, String> = mutableMapOf()

        when {
            bmiValue < 18.5 -> bmi[bmiValue] = "Underweight"
            bmiValue in 18.5..24.9 -> bmi[bmiValue] = "HealthyRange"
            bmiValue in 25.0..29.9 -> bmi[bmiValue] = "Overweight"
            bmiValue >29.9 -> bmi[bmiValue] = "Obesity"


        }
    return bmi
    }


}