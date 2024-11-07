package org.agileSoftDev.utills

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.agileSoftDev.domain.Activity
import org.agileSoftDev.domain.HealthIndicator
import org.agileSoftDev.domain.User
import org.agileSoftDev.domain.db.Activities
import org.agileSoftDev.domain.db.HealthIndicators
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

fun mapToHealthIndicator(it: ResultRow): HealthIndicator? {

    val indicatorid =it[HealthIndicators.indicatorid]
    val userid = it[HealthIndicators.userid]
    val age = it[HealthIndicators.age]
    val height = it[HealthIndicators.height]
    val weight = it[HealthIndicators.weight]
    val boxygen = it[HealthIndicators.boxygen]
    val hdl = it[HealthIndicators.hdl]
    val ldl = it[HealthIndicators.ldl]
    val alt = it[HealthIndicators.alt]
    val ast = it[HealthIndicators.ast]
    val gfr = it[HealthIndicators.gfr]

    return HealthIndicator(indicatorid,userid,age,height,weight,boxygen,hdl,ldl,ast,alt,gfr)



}


fun writeAsString (func : () -> Any ) :String{
    val mapper = jacksonObjectMapper().registerModules(JodaModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false)
    return  mapper.writeValueAsString(func())
}


