package org.agileSoftDev.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.javalin.http.Context
import org.agileSoftDev.domain.Activity
import org.agileSoftDev.domain.User
import org.agileSoftDev.domain.repository.ActivityDAO
import org.agileSoftDev.domain.repository.UserDAO
import org.agileSoftDev.utills.writeAsString
import org.postgresql.util.PSQLException

class healthTrackerController {
    private val userDAO = UserDAO()
    private val activityDAO = ActivityDAO()
    fun getAllUsers(ctx: Context)  {
            var users = userDAO.allUsers()
            if(users != null) ctx.json(200).json(mapOf(Pair("message","success"),Pair("data", users)))
            else ctx.status(204).json(mapOf("message" to "No users found"))
    }
    fun getUserById(ctx: Context) {
        val user = userDAO.getUserById(ctx.pathParam("id").toInt())
        if (user != null) ctx.status(200).json(mapOf(Pair("message","success"),Pair("data", user)))
        else ctx.status(404).json(mapOf(Pair("message","No user found"),Pair("data", null)))
    }
    fun findUserByEmail(ctx: Context) {
        val email = ctx.pathParam("email")
        val user = userDAO.findByEmail(email)
        if(user != null) ctx.status(200).json(mapOf(Pair("message","success"),Pair("data", user)))
        else  ctx.status(404).json(mapOf(Pair("message","No user found"),Pair("data", null)))
    }
    fun createUser(ctx: Context) {
     try{
         val mapper = jacksonObjectMapper()
         var user = mapper.readValue<User>(ctx.body())
         userDAO.addUser(user)
         ctx.status(201).json(mapOf("message" to "User created"))
     }
     catch (e : PSQLException) {
         ctx.status(500).json(mapOf("message" to "Error creating user, ${e.message}"))
     }
    }
    fun deleteUser(ctx: Context) {
       try {
           val userID = ctx.pathParam("id").toInt()
           if (userID != null) {
               userDAO.deleteUserById(userID)
               ctx.status(200).json(mapOf("message" to "User deleted"))
           }
           else ctx.status(400).json(mapOf("message" to "Error, User not found"))
       }
       catch (e : PSQLException) {
           ctx.status(500).json(mapOf("message" to "User deleteion unscuccesfull"))
       }
    }
    fun updateUser(ctx: Context) {
        try {
            val userId = ctx.pathParam("id").toInt()
            if(userId != null) {

                val mapper = jacksonObjectMapper()
                var user = mapper.readValue<User>(ctx.body())
                userDAO.updateUser(userId,user)
                ctx.status(200).json(mapOf("message" to "User updated"))
            }
            else ctx.status(400).json(mapOf("message" to "Error, User not found"))
        }
        catch (e : PSQLException) {
            ctx.status(500).json(mapOf("message" to "User updation unsuccesful, ${e.message}"))
        }
    }
    //Activities
    fun getAllActivities(ctx: Context) {
        val activities = writeAsString { activityDAO.getAllActivities() }
        ctx.status(200).json(mapOf(Pair("message" , "Success"),Pair("data", activities)))
    }
    fun getActivityByUser(ctx: Context) {
        var userId = ctx.pathParam("id").toInt()
        val activities = activityDAO.getActivityByUser(userId)
        if (activities.size > 0) {
            val parsedActivities = writeAsString { activities }
            ctx.status(200).json(mapOf(Pair("message", "Success"), Pair("data", activities)))
        } else ctx.status(404).json(mapOf(Pair("message", "No activities for the user"), Pair("data", null)))
    }


        fun addactivity(ctx: Context) {
            println("jbkbk")
            val mapper = jacksonObjectMapper().registerModules(JodaModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            activityDAO.saveActivity(mapper.readValue<Activity>(ctx.body()))
            ctx.status(201).json(mapOf("message" to "success"))

        }
    fun deleteActivityByUser(ctx: Context){
        var userId = ctx.pathParam("id").toInt()
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