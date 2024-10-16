package org.agileSoftDev.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.javalin.http.Context
import org.agileSoftDev.domain.User
import org.agileSoftDev.domain.repository.UserDAO

class healthTrackerController {


    private val userDAO = UserDAO()

    fun getAllUsers(ctx: Context)  {

        if (ctx.queryParam("email") != null) {

            val users = userDAO.findByEmail(ctx.queryParam("email"))
            if (users != null) {
                ctx.status(200).json(users)
            }
            else ctx.status(404).json(mapOf("message" to "Error,No users found"))
        } else
        {

            userDAO.allUsers()?.let { ctx.json(it) }


        }


    }

    fun getUserById(ctx: Context) {

        val user = userDAO.getUserById(ctx.pathParam("id").toInt())
        if (user != null) ctx.status(200).json(user)
        else ctx.status(400).json(mapOf("message" to "Error, No user found"))


    }

    fun createUser(ctx: Context) {
        try {
            val mapper = jacksonObjectMapper()
            val user: User = mapper.readValue<User>(ctx.body())
            userDAO.addUser(user)
        } catch (e: Exception) {
            ctx.status(400).json(mapOf("message" to "Error creating user"))
        }
    }

    fun searchByEmail(ctx: Context) {

        try {

            val user = userDAO.findByEmail(ctx.pathParam("email"))
            if (user != null) ctx.status(200).json(user)
            else ctx.status(400).json(mapOf("message" to "Error, No user found"))
        } catch (e: Exception) {
            ctx.status(400).json(mapOf("message" to "Error, User not found"))
        }
    }

    fun updateUser(ctx: Context) {

        try {
            val id = ctx.pathParam("id").toInt()
            val userFound = userDAO.getUserById(id)
            if (userFound == null) ctx.status(404).json(mapOf("message" to "Error, User not found"))
            else {

                val mapper = jacksonObjectMapper()
                val user = mapper.readValue<User>(ctx.body())

                val users = userDAO.updateUser(id, user)
                if (users != null) ctx.status(200).json(mapOf("message" to "Success,Updated user"))
                else ctx.status(400).json(mapOf("message" to "Error, No user found"))


            }

        } catch (ex: Exception) {
            ctx.status(400).json(mapOf("message" to "Error, Updating user failed, ${ex.localizedMessage}"))
        }
    }


}