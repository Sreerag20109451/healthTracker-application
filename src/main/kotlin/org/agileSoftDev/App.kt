package org.agileSoftDev

import org.agileSoftDev.config.DBConfig
import org.agileSoftDev.config.JavalinConfig

fun main(args : Array<String>) {


    DBConfig().getConnection()
    JavalinConfig().startJavalinInstance()


}