package org.agileSoftDev.utills

import org.agileSoftDev.domain.User
import org.agileSoftDev.domain.db.Users
import org.jetbrains.exposed.sql.ResultRow

fun mapToUser(it : ResultRow): User {
    var id = it[Users.id]
    var name = it[Users.name]
    var email = it[Users.email]

    return User(id, name, email)

}