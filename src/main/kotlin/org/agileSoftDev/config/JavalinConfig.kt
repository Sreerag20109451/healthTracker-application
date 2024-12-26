package org.agileSoftDev.config

import io.javalin.Javalin
import io.javalin.json.JavalinJackson
import org.agileSoftDev.controller.*
import org.agileSoftDev.utills.AuthenticationUtils.JWTutils
import org.agileSoftDev.utills.AuthenticationUtils.jsonObjectMapper

class JavalinConfig {
    private val controller = UserController()
    private val jwtObj = JWTutils()
    private val accessController = AuthorizationController()
    private val authenticationController = AuthenticationController()
    private val activityController = ActivityController()
    private val healthIndicatorController = HealthIndicatorController()
    private val healthRiskController = HealthRiskController()
    private val dietController = DietController()

    //https://javalin.io/documentation#before-handlers - reference from Javalin documentation

    fun startJavalinInstance(): Javalin {
        val app = Javalin.create {
            it.jsonMapper(JavalinJackson(jsonObjectMapper()))
            it.bundledPlugins.enableCors { cors ->
             cors.addRule{ crs ->
                    crs.allowHost("http://localhost:8080/","https://frontend-healthtracker-production.up.railway.app/")
                    crs.allowCredentials = true
                }
            }

        }.apply {
        }.start(getRemoteAssignedPort())
        registerRoutes(app)
        return app
    }

    fun registerRoutes(app: Javalin) {
        //Login and LogOut
        app.post("/api/logout", authenticationController::logout)
        app.post("/api/login", authenticationController::login)
        //User API Endpoints
        //No privilege
        app.post("/api/users", controller::createUser)
        //OnlyAdminPrivileges
        app.get("/api/users") { ctx ->
            println("its here")
            if (jwtObj.verifyTokens(ctx)) {
                println("JWT verified")
                accessController.adminOnlyPrivilegeCheck(ctx, controller::getAllUsers)
            }
            else ctx.status(401).json(mapOf("message" to "Authentication error, invalid token!"))
        }
        app.get("/api/users/{userID}/details") { ctx ->
            if (jwtObj.verifyTokens(ctx)) accessController.adminAndSameUserPrivilegeCheck(ctx, controller::getDetails)
            else ctx.status(401).json(mapOf("message" to "Authentication error, invalid token!"))
        }


        app.get("/api/users/email/{email}") { ctx ->
            if (jwtObj.verifyTokens(ctx)) accessController.adminOnlyPrivilegeCheck(ctx, controller::findUserByEmail)
            else ctx.status(401).json(mapOf("message" to "Authentication error, invalid token!"))
        }

        app.put("/api/users/{userID}") { ctx ->
            if (jwtObj.verifyTokens(ctx)) accessController.adminAndSameUserPrivilegeCheck(ctx, controller::updateUser)
            else ctx.status(401).json(mapOf("message" to "Authentication Error, invalid token"))
        }

        //Admin and Same user privileges (Always verify token)
        app.get("/api/users/{userID}") { ctx ->
            if (jwtObj.verifyTokens(ctx)) accessController.adminAndSameUserPrivilegeCheck(ctx, controller::getUserById)
            else ctx.status(401).json(mapOf("message" to "Authentication Error, invalid token"))
        }

        app.delete("/api/users/{userID}") { ctx ->
            if (jwtObj.verifyTokens(ctx)) accessController.adminAndSameUserPrivilegeCheck(ctx, controller::deleteUser)
            else ctx.status(401).json(mapOf("message" to "Authentication Error, invalid token"))
        }
        //ActivityEndpoints
        //AdminOnlyPrivileges
        app.get("/api/activities") { ctx ->
            if (jwtObj.verifyTokens(ctx)) accessController.adminOnlyPrivilegeCheck(
                ctx,
                activityController::getAllActivities
            )
            else ctx.status(401).json(mapOf("message" to "Authentication error, invalid token!"))
        }

        //AdminAndSameUserPrivilege

        //Admin and Same user privilege
        app.post("/api/users/{userID}/activities") { ctx ->
            if (jwtObj.verifyTokens(ctx)) accessController.adminAndSameUserPrivilegeCheck(
                ctx,
                activityController::addactivity
            )
            else ctx.status(401).json(mapOf("message" to "Authentication Error, invalid token"))
        }
        app.get("api/users/{userID}/activities") { ctx ->
            if (jwtObj.verifyTokens(ctx)) accessController.adminAndSameUserPrivilegeCheck(
                ctx,
                activityController::getActivityByUser
            )
            else ctx.status(401).json(mapOf("message" to "Authentication Error, invalid token"))
        }
        app.delete("api/users/{userID}/activities/{actId}") { ctx ->
            if (jwtObj.verifyTokens(ctx)) accessController.adminAndSameUserPrivilegeCheck(
                ctx,
                activityController::deleteActivityByUser
            )
            else ctx.status(401).json(mapOf("message" to "Authentication Error, invalid token"))
        }

        //HealthIndicators

        //AdminOnly


        //AdminAndSameUserPrivilege


        app.get("/api/users/{userID}/healthindicators") { ctx ->
            if (jwtObj.verifyTokens(ctx)) accessController.adminAndSameUserPrivilegeCheck(
                ctx,
                healthIndicatorController::getHealthIndicatorsByUser
            )
            else ctx.status(401).json(mapOf("message" to "Authentication Error, invalid token"))

        }
        app.post("/api/users/{userID}/healthindicators") { ctx ->
            if (jwtObj.verifyTokens(ctx)) accessController.adminOnlyPrivilegeCheck(
                ctx,
                healthIndicatorController::addIndicatorsforUser
            )
            else ctx.status(401).json(mapOf("message" to "Authentication Error, invalid token"))

        }

        //Risks

        //AdminAndSameUserPrivilege

        app.get("/api/users/{userID}/risks") { ctx ->
            if (jwtObj.verifyTokens(ctx)) accessController.adminAndSameUserPrivilegeCheck(
                ctx,
                healthRiskController::getRisks
            )
            else ctx.status(401).json(mapOf("message" to "Authentication Error, invalid token"))
        }


        //Diets

        app.get("/api/users/{userID}/diets") { ctx ->
            if (jwtObj.verifyTokens(ctx)) accessController.adminAndSameUserPrivilegeCheck(
                ctx,
                dietController::suggestDiets
            )
            else ctx.status(401).json(mapOf("message" to "Authentication Error, invalid token"))

        }


        //Errors and exceptions
        app.get("/*") { ctx ->
            ctx.status(404).json(mapOf("message" to "Path not found"))
        }


        app.error(500) { ctx ->
            ctx.status(500).json(mapOf("message " to "ServerError"))
        }
    }

    private fun getRemoteAssignedPort(): Int {
        val remotePort = System.getenv("PORT")
        return if (remotePort != null) {
            Integer.parseInt(remotePort)
        } else 8085
    }


}