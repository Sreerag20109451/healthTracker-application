package org.agileSoftDev.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.javalin.http.Context
import org.agileSoftDev.domain.HealthIndicator
import org.agileSoftDev.domain.repository.HealthIndicatorDAO
import org.agileSoftDev.domain.repository.UserDAO
import org.postgresql.util.PSQLException

class HealthIndicatorController {
    private var userDAO = UserDAO()
    private val authorizationController = AuthorizationController()
    private  val healthIndicatorDAO =  HealthIndicatorDAO()
    fun addIndicatorsforUser(ctx: Context) {
     try {
         var mapper = jacksonObjectMapper()

         val healthIndicator: HealthIndicator = mapper.readValue<HealthIndicator>(ctx.body())
         var userID = ctx.pathParam("userID").toInt()
         var user = userDAO.getUserById(userID)
         if (user == null) {
             ctx.status(404).json(mapOf(Pair("message", "User not found")))
         } else {
             healthIndicatorDAO.addHealthIndicatorByUser(userID, healthIndicator)
             ctx.status(201).json(mapOf(Pair("message", "Healthindicators added")))
         }
     }
     catch (e :PSQLException){

         ctx.status(500).json(mapOf(Pair("message", "Error while adding health indicators")))
     }

    }
    fun getHealthIndicatorsByUser(ctx: Context) {
        var userID = ctx.pathParam("userID").toInt()
        if(userDAO.getUserById(userID) == null) {
            ctx.status(404).result("User not found")
            return
        }
        var healthIndicators  = healthIndicatorDAO.getHealthIndicatorsByUser(userID)
        if(healthIndicators == null) ctx.status(200).json(mapOf(Pair("message","no indicators"),Pair("data", healthIndicators)))
        else ctx.status(200).json(mapOf(Pair("message","indicators retrieved"),Pair("data", healthIndicators)))

    }

}