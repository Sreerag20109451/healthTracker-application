package org.agileSoftDev.config

import io.javalin.Javalin
import io.javalin.http.Context
import io.javalin.http.Handler
import io.javalin.json.JavalinJackson
import org.agileSoftDev.controller.AccessManagementController
import org.agileSoftDev.controller.AuthenticationController
import org.agileSoftDev.controller.healthTrackerController
import org.agileSoftDev.utills.JWTutils
import org.agileSoftDev.utills.cookies.CookieStore
import org.agileSoftDev.utills.jsonObjectMapper
import org.postgresql.util.PSQLException

class JavalinConfig {


    private val controller = healthTrackerController()
    private  val jwtObj = JWTutils()
    private val accessController =  AccessManagementController()
    private val authenticationController = AuthenticationController()

    fun startJavalinInstance() : Javalin{
        val app = Javalin.create{ it.jsonMapper(JavalinJackson(jsonObjectMapper()))}.apply {  }.start(getRemoteAssignedPort())
        registerRoutes(app)
        return  app
    }
    fun adminAndSameUserPrivilegeCheck(ctx: Context, function: (Context) -> Unit) {
            if(!accessController.CheckAdminRole(ctx)){
                if (!accessController.checkSameUserloggedIn(ctx)) {
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

        if(accessController.CheckAdminRole(ctx)) function(ctx)
        else ctx.status(403).json(mapOf("message" to "Access denied!"))
    }
    fun registerRoutes(app: Javalin) {
        //Login and LogOut
        app.post("/api/logout", authenticationController::logout)
        app.post("/api/login", authenticationController::login)
        //User API Endpoints
        //No privilege
        app.post("/api/users", controller::createUser)
        //OnlyAdminPrivileges
        app.get("/api/users"){ctx ->
            if(jwtObj.verifyTokens(ctx)) adminOnlyPrivilegeCheck(ctx,controller::getAllUsers)
            else ctx.status(403).json(mapOf("message" to "Authentication error, invalid token!"))
        }
        app.get("/api/users/email/{email}"){ctx ->
            if(jwtObj.verifyTokens(ctx)) adminOnlyPrivilegeCheck(ctx,controller::findUserByEmail)
            else ctx.status(403).json(mapOf("message" to "Authentication error, invalid token!"))
        }
        //Admin and Same user privileges (Always verify token)
        app.get("/api/users/{userID}"){ ctx ->
            if(jwtObj.verifyTokens(ctx)) adminAndSameUserPrivilegeCheck(ctx,controller::getUserById)
           else ctx.status(403).json(mapOf("message" to "Authentication Error, invalid token"))
        }
        app.put("/api/users/{userID}"){ ctx ->
            if(jwtObj.verifyTokens(ctx)) adminAndSameUserPrivilegeCheck(ctx,controller::updateUser)
            else ctx.status(403).json(mapOf("message" to "Authentication Error, invalid token"))
        }
        app.delete("/api/users/{userID}"){ ctx ->
            if(jwtObj.verifyTokens(ctx)) adminAndSameUserPrivilegeCheck(ctx,controller::deleteUser)
            else ctx.status(403).json(mapOf("message" to "Authentication Error, invalid token"))
        }



        //ActivityEndpoints

        //AdminOnlyPrivileges
        app.get("/api/activities"){ctx ->
            if(jwtObj.verifyTokens(ctx)) adminOnlyPrivilegeCheck(ctx,controller::getAllActivities)
            else ctx.status(403).json(mapOf("message" to "Authentication error, invalid token!"))

        }
        //Admin and Same user privilege
        app.post("/api/users/{userID}/activities"){ ctx ->
            if(jwtObj.verifyTokens(ctx)) adminAndSameUserPrivilegeCheck(ctx,controller::addactivity)
            else ctx.status(403).json(mapOf("message" to "Authentication Error, invalid token"))
        }
        app.get("api/users/{userID}/activities"){ ctx ->
            if(jwtObj.verifyTokens(ctx)) adminAndSameUserPrivilegeCheck(ctx,controller::getActivityByUser)
            else ctx.status(403).json(mapOf("message" to "Authentication Error, invalid token"))
        }
        app.delete("api/users/{userID}/activities/{actId}"){ ctx ->
            if(jwtObj.verifyTokens(ctx)) adminAndSameUserPrivilegeCheck(ctx,controller::deleteActivityByUser)
            else ctx.status(403).json(mapOf("message" to "Authentication Error, invalid token"))
        }



        //Errors and exceptions

        app.exception(PSQLException::class.java){
            e, ctx -> ctx.status(500).json(mapOf("message " to "SQLException"))
        }

        app.error(500){
            ctx -> ctx.status(500).json(mapOf("message " to "ServerError"))
        }
        app.error(404){
         ctx -> ctx.json(mapOf("message" to "404, patth not found"))
          }
    }

    private fun getRemoteAssignedPort(): Int {
        val remotePort = System.getenv("PORT")
        return if (remotePort != null) {
            Integer.parseInt(remotePort)
        } else 8081
    }




}