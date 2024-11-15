package org.agileSoftDev.controller

import io.javalin.http.Context
import org.agileSoftDev.domain.repository.HealthRiskDAO

class HealthRiskController {

    var riskDAO = HealthRiskDAO()
    fun getRisks (ctx: Context){
        val userID = ctx.pathParam("userID").toInt()
        val risks =  riskDAO.getPossibleHealthRisks(userID)
        if((risks == null) or risks?.isEmpty()!! ) ctx.status(200).json(mapOf(Pair("status","success"),Pair("message", "No health risks found for user")))
        else{
            ctx.status(200).json(mapOf(Pair("status" , "success"),Pair("message","Health risks found for user"),Pair("data", risks)))
        }
    }


}