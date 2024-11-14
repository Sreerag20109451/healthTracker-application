package org.agileSoftDev.domain.db

import org.jetbrains.exposed.sql.Table




object Users: Table("users") {

    val id = integer("id").autoIncrement()
    var name = varchar("name", 20)
    var email = varchar("email", 50)
    val password = varchar("password", 100)
    val role = varchar("role", 30    ).default("user")
    override val primaryKey: PrimaryKey = PrimaryKey(id)

}