package org.agileSoftDev.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.javalin.http.Context
import org.agileSoftDev.domain.HealthIndicator
import org.agileSoftDev.domain.repository.HealthIndicatorDAO
import org.agileSoftDev.domain.repository.UserDAO

class HealthIndicatorController {

    private var userDAO = UserDAO()
    private val authorizationController = AuthorizationController()
    private  val healthIndicatorDAO =  HealthIndicatorDAO()

    fun addIndicatorsforUser(ctx: Context) {
        var mapper = jacksonObjectMapper()
       val healthIndicator: HealthIndicator = mapper.readValue<HealthIndicator>(ctx.body())
      var userID = ctx.pathParam("userID").toInt()
      var user =  userDAO.getUserById(userID)
      if(user == null) {
          ctx.status(404).json(mapOf(Pair("status" , "error"),Pair("message" , "User not found"),Pair("data", null)))
      }
      else{
          healthIndicatorDAO.addHealthIndicatorByUser(userID,healthIndicator)
          ctx.status(201).json(mapOf(Pair("status","success"), Pair("message","Healthindicators added")))
      }

    }
    fun getHealthIndicatorsByUser(ctx: Context) {
        var userID = ctx.pathParam("userID").toInt()
        var healthIndicators  = healthIndicatorDAO.getHealthIndicatorsByUser(userID)
        if(healthIndicators == null) ctx.status(200).json(mapOf(Pair("status", "success"),Pair("message","no indicators"),Pair("data", healthIndicators)))
        else ctx.status(200).json(mapOf(Pair("status", "success"),Pair("message","indicators retrieved"),Pair("data", healthIndicators)))

    }
    


}