package org.agileSoftDev.domain.db

import org.agileSoftDev.domain.Activity
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNotNull
import org.jetbrains.exposed.sql.Table



object Users: Table("users") {

    val id = integer("id").autoIncrement()

    var name = varchar("name", 20)

    var email = varchar("email", 50)

    val role = varchar("role", 30    ).default("user")

    override val primaryKey: PrimaryKey = PrimaryKey(id)



}