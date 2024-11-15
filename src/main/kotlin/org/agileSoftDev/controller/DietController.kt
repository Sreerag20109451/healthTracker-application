package org.agileSoftDev.controller

import io.javalin.http.Context
import org.agileSoftDev.domain.repository.HealthRiskDAO


class DietController {

    private var riskDAO = HealthRiskDAO()
    fun suggestDiets  (ctx: Context){

        val userID = ctx.pathParam("userID").toInt()
        val risks = riskDAO.getPossibleHealthRisks(userID)
        val diets = riskDAO.getDiet(risks)
        if(diets == null) ctx.status(200).json(mapOf(Pair("status","success"),Pair("message", "No diets required"),Pair("data", diets)))
        else ctx.status(200).json(mapOf(Pair("status","success"),Pair("message", "Diets are found"),Pair("data", diets)))
    }

}