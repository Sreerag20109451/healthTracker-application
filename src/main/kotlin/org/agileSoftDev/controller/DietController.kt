package org.agileSoftDev.controller

import io.javalin.http.Context
import org.agileSoftDev.domain.repository.DietsDAO
import org.agileSoftDev.domain.repository.HealthRiskDAO


class DietController {

    private val riskDAO = HealthRiskDAO()
    private val dietsDAO = DietsDAO()
    fun suggestDiets  (ctx: Context){

        val userID = ctx.pathParam("userID").toInt()
        val risks = riskDAO.getPossibleHealthRisks(userID)
        val diets = dietsDAO.getDiet(risks)
        if(diets == null) ctx.status(200).json(mapOf(Pair("status","success"),Pair("message", "No diets required"),Pair("data", diets)))
        else ctx.status(200).json(mapOf(Pair("status","success"),Pair("message", "Diets are found"),Pair("data", diets)))
    }

}