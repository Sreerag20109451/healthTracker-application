package org.agileSoftDev.helpers

import io.javalin.Javalin
import org.agileSoftDev.config.JavalinConfig

object ServerContainer {

    private fun startServer(): Javalin {

       return JavalinConfig().startJavalinInstance()
    }
    val instance by lazy {

        startServer()

    }



}