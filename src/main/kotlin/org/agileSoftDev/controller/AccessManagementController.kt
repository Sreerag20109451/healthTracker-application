package org.agileSoftDev.controller

import io.javalin.http.Context
import org.agileSoftDev.domain.User
import org.agileSoftDev.utills.cookies.CookieStore

class AccessManagementController {

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
}



