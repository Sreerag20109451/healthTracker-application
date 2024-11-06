package org.agileSoftDev.controller

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.javalin.http.Context
import org.agileSoftDev.domain.Activity
import org.agileSoftDev.domain.repository.ActivityDAO
import org.agileSoftDev.domain.repository.UserDAO
import org.agileSoftDev.utills.Enums.checkActivities
import org.agileSoftDev.utills.writeAsString


class ActivityController {

    private  var activityDAO = ActivityDAO()
    private  var userDAO = UserDAO()

    fun getAllActivities(ctx: Context) { //Admin privilege + authentication required
        val activities = writeAsString { activityDAO.getAllActivities() }
        ctx.status(200).json(mapOf(Pair("message" , "Success"),Pair("data", activities)))
    }
    fun getActivityByUser(ctx: Context) { //Same user privilege
        var userId = ctx.pathParam("userID").toInt()
        val activities = activityDAO.getActivityByUser(userId)
        if (activities.size > 0) {
            val parsedActivities = writeAsString { activities }
            ctx.status(200).json(mapOf(Pair("message", "Success"), Pair("data", activities)))
        } else ctx.status(200).json(mapOf(Pair("message", "No activities for the user"), Pair("data", null)))
    }
    fun addactivity(ctx: Context) { //Same user privilege

        val mapper = jacksonObjectMapper().registerModules(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        var activity = mapper.readValue<Activity>(ctx.body())
        if(!checkActivities(activity.description)) ctx.status(400).json(mapOf("message" to "Error, the activity description is invalid"))
        else{
            activityDAO.saveActivity(activity)
            ctx.status(201).json(mapOf("message" to "success"))

        }

    }
    fun deleteActivityByUser(ctx: Context){ //Same user privilege
        var userId = ctx.pathParam("userID").toInt()
        var activityId = ctx.pathParam("actId").toInt()
        println(userId)
        println(activityId)
        var user = userDAO.getUserById(userId)
        if(user!= null) {
            println(user.id)
            var userActivities = activityDAO.getActivityByUser(userId)
            var activity = userActivities.find{it.id == activityId}
            println(activity)
            if(activity != null)
            {
                activityDAO.deleteActivityByUser(userId,activityId)
                ctx.json(200).json(mapOf("message" to "Activity deleted"))
            }
            else{
                ctx.json(400).json(mapOf("message" to "Error, No such activity for the user found"))
            }
        }
        else ctx.status(400).json(mapOf("message" to "Error, No user found"))



    }

}