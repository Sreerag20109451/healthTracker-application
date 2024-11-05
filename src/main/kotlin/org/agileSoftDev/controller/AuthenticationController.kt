package org.agileSoftDev.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.javalin.http.Context
import org.agileSoftDev.domain.User
import org.agileSoftDev.domain.repository.UserDAO
import org.agileSoftDev.utills.JWTutils
import org.agileSoftDev.utills.cookies.CookieStore

class AuthenticationController {
    private var userDAO = UserDAO()
    private var jwtObj = JWTutils()
    private var cookieStore = CookieStore()

    fun login(ctx: Context){ //all users
        try {
            var mapper = jacksonObjectMapper()
            val bodyObj: Map<String,String> = mapper.readValue(ctx.body())
            val email = bodyObj["email"]
            val password = bodyObj["password"]
            if(email != null && password != null) {
                var user = userDAO.loginUser(email,password)
                if(user == null)  ctx.status(403).json(mapOf("message" to "Invalid email or password"))
                else{
                    val token  = jwtObj.generateToken(user!!)

                    println(user)
                    println(user.role)
                    cookieStore.saveToCookieStore(ctx, User(user.id, user.name,user.email,user.password,user.role))
                    ctx.status(200).json(mapOf(Pair("message" , "${user?.name} is logged in"),Pair("token",token)))
                }
            }
            else{
                ctx.status(400).json(mapOf("message" to "Enter email and password"))
            }

        }catch (e: Exception){
            ctx.status(500).json(mapOf(Pair("message","Error logging in, ${e.message}" ),Pair("data", null)))
        }

    }


    fun logout(ctx: Context) {

        println("logging out")
        ctx.cookieStore().clear()

    }

}