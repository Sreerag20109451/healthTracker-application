package org.agileSoftDev.controller

import io.javalin.http.Context
import org.agileSoftDev.domain.HealthIndicator
import org.agileSoftDev.domain.db.HealthRisks
import org.agileSoftDev.domain.repository.HealthIndicatorDAO
import org.agileSoftDev.domain.repository.HealthRiskDAO
import org.agileSoftDev.domain.repository.UserDAO
import org.agileSoftDev.utills.selectDistinct
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greater
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

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
    fun suggestDiets  (ctx: Context){

        val userID = ctx.pathParam("userID").toInt()
        val risks = riskDAO.getPossibleHealthRisks(userID)
        val diets = riskDAO.getDiet(risks)
        if(diets == null) ctx.status(200).json(mapOf(Pair("status","success"),Pair("message", "No diets required"),Pair("data", diets)))
        else ctx.status(200).json(mapOf(Pair("status","success"),Pair("message", "Diets are found"),Pair("data", diets)))
    }

}