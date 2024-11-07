package org.agileSoftDev.domain

data class HealthIndicator (
    var indicatorid: Int, var userid : Int,  var age : Int, var height: Int?, var weight : Int?, var boxygen: Int?,  var hdl : Int?, var ldl : Int? , var alt : Int?, var ast : Int?, var gfr : Int?
)