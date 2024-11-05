package org.agileSoftDev.utills

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.agileSoftDev.domain.Activity
import org.agileSoftDev.domain.User
import org.agileSoftDev.domain.db.Activities
import org.agileSoftDev.domain.db.Users
import org.agileSoftDev.utills.Enums.UserRoles
import org.jetbrains.exposed.sql.Function
import org.jetbrains.exposed.sql.ResultRow

fun mapToUser(it : ResultRow): User {
    var id = it[Users.id]
    var name = it[Users.name]
    var email = it[Users.email]
    var password = it[Users.password]
    var role = it[Users.role]
    return User(id, name, email,password,role)
}
fun mapToActivities(it: ResultRow): Activity {
    val id = it[Activities.id]
    var description = it[Activities.description]
    var duration = it[Activities.duration]
    var calories = it[Activities.calories]
    var started = it[Activities.started]
    val userId = it[Activities.userId]
    return Activity(id,description,duration,calories,started,userId)
}
fun writeAsString (func : () -> Any ) :String{
    val mapper = jacksonObjectMapper().registerModules(JodaModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false)
    return  mapper.writeValueAsString(func())
}


