package org.agileSoftDev.controller

import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.javalin.http.Context
import javalinjwt.JavalinJWT
import org.agileSoftDev.domain.Activity
import org.agileSoftDev.domain.User
import org.agileSoftDev.domain.repository.ActivityDAO
import org.agileSoftDev.domain.repository.UserDAO
import org.agileSoftDev.utills.Enums.checkActivities
import org.agileSoftDev.utills.Enums.checkRole
import org.agileSoftDev.utills.JWTutils
import org.agileSoftDev.utills.cookies.CookieStore
import org.agileSoftDev.utills.writeAsString
import org.postgresql.util.PSQLException
import kotlin.reflect.typeOf

class healthTrackerController {
    private val userDAO = UserDAO()
    private val activityDAO = ActivityDAO()

    fun getAllUsers(ctx: Context)  {      //AdminPrivilege+ Authentication required
            var users = userDAO.allUsers()
            if(users != null) ctx.json(200).json(mapOf(Pair("message","success"),Pair("data", users)))
            else ctx.status(204).json(mapOf("message" to "No users found"))
    }
    fun getUserById(ctx: Context) {   //AdminPrivilege + Authentication required
        val user = userDAO.getUserById(ctx.pathParam("userID").toInt())
        if (user != null) ctx.status(200).json(mapOf(Pair("message","success"),Pair("data", user)))
        else ctx.status(404).json(mapOf(Pair("message","No user found"),Pair("data", null)))
    }
    fun findUserByEmail(ctx: Context) { //AdminPrivilege + Authentication required
        val email = ctx.pathParam("email")
        val user = userDAO.findByEmail(email)
        if(user != null) ctx.status(200).json(mapOf(Pair("message","success"),Pair("data", user)))
        else  ctx.status(404).json(mapOf(Pair("message","No user found"),Pair("data", null)))
    }
    fun createUser(ctx: Context) {   // Authentication required
     try{
         val mapper = jacksonObjectMapper()
         var user = mapper.readValue<User>(ctx.body())
         if(!checkRole(user.role)) ctx.status(400).json(mapOf("message" to "Error, the user cannot have this role"))
         else{
             userDAO.addUser(user)
             ctx.status(201).json(mapOf("message" to "User created"))
         }
     }
     catch (e : PSQLException) {
         ctx.status(500).json(mapOf("message" to "Error creating user, ${e.message}"))
     }
    }
    fun deleteUser(ctx: Context) {  //Same user privilege + authentication required
       try {
           val userID = ctx.pathParam("userID").toInt()
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
    fun updateUser(ctx: Context) { //Same user privilege + authentication required
        try {
                val userId = ctx.pathParam("userID").toInt()
                if (userId != null) {
                    val mapper = jacksonObjectMapper()
                    var user = mapper.readValue<User>(ctx.body())
                    userDAO.updateUser(userId, user)
                    ctx.status(200).json(mapOf("message" to "User updated"))
            }
        }
        catch (e : PSQLException) {
            ctx.status(500).json(mapOf("message" to "User updation unsuccesful, ${e.message}"))
        }
    }
    //Activities
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