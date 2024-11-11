package org.agileSoftDev.controller

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.javalin.http.Context
import org.agileSoftDev.domain.Activity
import org.agileSoftDev.domain.User
import org.agileSoftDev.domain.repository.ActivityDAO
import org.agileSoftDev.domain.repository.UserDAO
import org.agileSoftDev.utills.Enums.checkActivities
import org.agileSoftDev.utills.Enums.checkRole
import org.agileSoftDev.utills.writeAsString
import org.postgresql.util.PSQLException

class healthTrackerController {
    private val userDAO = UserDAO()
    private val activityDAO = ActivityDAO()

    fun getAllUsers(ctx: Context)  {      //AdminPrivilege+ Authentication required
            var users = userDAO.allUsers()
            if(users != null) ctx.json(200).json(mapOf(Pair("message","success"),Pair("data", users)))
            else ctx.status(200).json(mapOf("message" to "No users found"))
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
           else ctx.status(404).json(mapOf("message" to "Error, User not found"))
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
            ctx.status(404).json(mapOf("message" to "User updation unsuccesful, ${e.message}"))
        }
    }


}