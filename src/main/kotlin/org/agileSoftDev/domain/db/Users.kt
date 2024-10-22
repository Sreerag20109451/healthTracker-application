package org.agileSoftDev.domain.db

import org.jetbrains.exposed.sql.Table

object Users: Table("users") {

    val id = integer("id").autoIncrement()

    var name = varchar("name", 20)

    var email = varchar("email", 50)
    override val primaryKey: PrimaryKey = PrimaryKey(id)



}