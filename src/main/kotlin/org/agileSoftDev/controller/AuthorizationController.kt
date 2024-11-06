package org.agileSoftDev.controller

import io.javalin.http.Context
import org.agileSoftDev.utills.cookies.CookieStore

class AuthorizationController {

    private var cookieStore = CookieStore()

    fun CheckAdminRole(ctx: Context): Boolean {
        if (cookieStore.getFromCookieStore(ctx, "user") == null) return false
        else {
            println(cookieStore.getFromCookieStore(ctx, "user"))
            var user = cookieStore.getFromCookieStore(ctx, "user")
            println(user)
            var role = user!!.role
            return role == "admin"
        }
    }

    fun checkSameUserloggedIn(ctx: Context): Boolean {
        if (cookieStore.getFromCookieStore(ctx, "user") == null) return false
        else {
            var userIDResourceBeingchecked = ctx.pathParam("userID").toInt()
            var userIDloggedIn = cookieStore.getFromCookieStore(ctx, "user")?.id
            return userIDResourceBeingchecked == userIDloggedIn

        }

    }

     fun adminAndSameUserPrivilegeCheck(ctx: Context, function: (Context) -> Unit) {
        if(!CheckAdminRole(ctx)){
            if (!checkSameUserloggedIn(ctx)) {
                ctx.status(403).json(mapOf("message" to "Access denied!"))
            }
            else{
                function(ctx)
            }
        }
        else{
            ctx.status(403).json(mapOf("message" to "Access denied!"))
        }
    }
     fun adminOnlyPrivilegeCheck(ctx: Context, function: (Context) -> Unit) {

        if(!CheckAdminRole(ctx)) function(ctx)
        else ctx.status(403).json(mapOf("message" to "Access denied!"))
    }
}



