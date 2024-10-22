package org.agileSoftDev.domain.db

import org.jetbrains.exposed.sql.Table

object Users: Table("users") {

    val id = integer("id").autoIncrement()

    val name = varchar("name", 20)

    val email = varchar("email", 50)
    override val primaryKey: PrimaryKey = PrimaryKey(id)



}