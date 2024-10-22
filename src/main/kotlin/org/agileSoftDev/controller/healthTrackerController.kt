package org.agileSoftDev.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.javalin.http.Context
import org.agileSoftDev.domain.User
import org.agileSoftDev.domain.repository.UserDAO
import org.postgresql.util.PSQLException

class healthTrackerController {


    private val userDAO = UserDAO()

    fun getAllUsers(ctx: Context)  {

            var users = userDAO.allUsers()
            if(users != null) ctx.json(200).json(users)
            else ctx.status(204).json(mapOf("message" to "No users found"))
    }

    fun getUserById(ctx: Context) {
        val user = userDAO.getUserById(ctx.pathParam("id").toInt())
        if (user != null) ctx.status(200).json(user)
        else ctx.status(204).json(mapOf("message" to "Error, No user found"))
    }

    fun findUserByEmail(ctx: Context) {
        val email = ctx.pathParam("email")
        val user = userDAO.findByEmail(email)
        if(user != null) ctx.status(200).json(user)
        else ctx.status(404).json(mapOf("message" to "Error, User not found"))
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
}