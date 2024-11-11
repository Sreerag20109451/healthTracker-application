package org.agileSoftDev.domain


data class HealthRisk (
    var riskid: Int, var risk : String, var bmi : Double?, var boxygen : Int?,  var hdl : Int?, var ldl : Int?, var alt: Int?, var ast: Int?, var gfr : Int?, var dept : String, var  dietid : Int
)