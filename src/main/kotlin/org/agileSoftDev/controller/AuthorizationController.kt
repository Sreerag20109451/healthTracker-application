package org.agileSoftDev.controller

import io.javalin.http.Context
import org.agileSoftDev.domain.User

class AuthorizationController {
    private var cookieStore = CookieController()
    private fun CheckAdminRole(ctx: Context): Boolean {
        println("checking for user")
        println(" the user in cookie store is ${cookieStore.getFromCookieStore(ctx, "user")}")
        if (cookieStore.getFromCookieStore(ctx, "user")==null) {
            println("this is if no user found in server")
            return false
        }
        else {
            println("Now it is working")
            val user = cookieStore.getFromCookieStore(ctx, "user")
            val role = user!!.role
            return role == "admin"
        }
    }
    fun checkSameUserloggedIn(ctx: Context): Boolean {
        if (cookieStore.getFromCookieStore(ctx, "user") == null) return false
        else {
            val userIDResourceBeingchecked = ctx.pathParam("userID").toInt()
            val userIDloggedIn = cookieStore.getFromCookieStore(ctx, "user")?.id
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



