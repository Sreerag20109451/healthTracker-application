package org.agileSoftDev.config

import io.javalin.Javalin
import org.agileSoftDev.controller.healthTrackerController

class JavalinConfig {


    private val controller = healthTrackerController()

    fun startJavalinInstance() : Javalin{

        val app = Javalin.create().apply {  }.start(getRemoteAssignedPort())
        registerRoutes(app)

        return  app
    }


    fun registerRoutes(app: Javalin) {

        app.get("/api/users", controller::getAllUsers)
        app.get("/api/users/{id}", controller :: getUserById)
        app.post("/api/users", controller::createUser)
        app.put("/api/users/{id}", controller::updateUser)


    }

    private fun getRemoteAssignedPort(): Int {
        val remotePort = System.getenv("PORT")
        return if (remotePort != null) {
            Integer.parseInt(remotePort)
        } else 8080
    }

}