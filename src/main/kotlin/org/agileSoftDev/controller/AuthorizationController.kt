package org.agileSoftDev.controller

import CookieController
import io.javalin.http.Context
import org.agileSoftDev.domain.User
import org.agileSoftDev.domain.repository.UserDAO

class AuthorizationController {
    private val userDAO = UserDAO()
    private var cookieStore = CookieController()
    private fun sessionUser(ctx: Context) : User? {

        val sessionID = ctx.header("Sessionid")
        if (sessionID != null) {
            return  userDAO.getUserById(sessionID.toInt())
        }
        return null
    }

    private fun CheckAdminRole(ctx: Context): Boolean {
        val sessionUser = sessionUser(ctx)
        println("checking for user")
        println(" the user in cookie store is ${sessionUser}")

        println(sessionUser)
        if (sessionUser ==null) {
            println("this is if no user found in server")
            return false
        }
        else {
            println("Now it is working")
            val role = sessionUser.role
            return role == "admin"
        }
    }
    fun checkSameUserloggedIn(ctx: Context): Boolean {
        val sessionUser = sessionUser(ctx)
        if (sessionUser == null) return false
        else {
            val userIDResourceBeingchecked = ctx.pathParam("userID").toInt()
            val userIDloggedIn = sessionUser.id
            return userIDResourceBeingchecked == userIDloggedIn

        }

    }
     fun adminAndSameUserPrivilegeCheck(ctx: Context, function: (Context) -> Unit) {
        if(!CheckAdminRole(ctx)){
           println("user is not admin")
            if (!checkSameUserloggedIn(ctx)) {
                println("user is not same user either")
                ctx.status(403).json(mapOf("message" to "Access denied!"))
            }
            else{
                function(ctx)
            }
        }
        else{
            function(ctx)
        }
    }
     fun adminOnlyPrivilegeCheck(ctx: Context, function: (Context) -> Unit) {

        if(CheckAdminRole(ctx)) function(ctx)
        else ctx.status(403).json(mapOf("message" to "Access denied!"))
    }

    fun userIsNotAdmin(user: User): Boolean{

        return  user.role != "admin"
    }

}



