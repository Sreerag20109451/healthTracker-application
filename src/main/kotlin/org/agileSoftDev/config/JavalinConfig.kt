package org.agileSoftDev.config

import io.javalin.Javalin
import io.javalin.json.JavalinJackson
import org.agileSoftDev.controller.healthTrackerController
import org.agileSoftDev.utills.jsonObjectMapper

class JavalinConfig {


    private val controller = healthTrackerController()

    fun startJavalinInstance() : Javalin{

        val app = Javalin.create{ it.jsonMapper(JavalinJackson(jsonObjectMapper()))}.apply {  }.start(getRemoteAssignedPort())
        registerRoutes(app)

        return  app
    }


    fun registerRoutes(app: Javalin) {

        app.get("/api/users", controller::getAllUsers)
        app.get("/api/users/{id}", controller :: getUserById)
        app.get("/api/users/email/{email}", controller :: findUserByEmail)


        app.post("/api/users", controller::createUser)
        app.put("/api/users/{id}", controller::updateUser)
        app.delete("/api/users/{id}", controller::deleteUser)

        app.get("/api/activities", controller:: getAllActivities)
        app.post("/api/activities", controller::addactivity)
        app.get("api/users/{id}/activities",controller:: getActivityByUser)
        app.delete("api/users/{id}/activities/{actId}",controller:: deleteActivityByUser)


    }

    private fun getRemoteAssignedPort(): Int {
        val remotePort = System.getenv("PORT")
        return if (remotePort != null) {
            Integer.parseInt(remotePort)
        } else 8081
    }




}