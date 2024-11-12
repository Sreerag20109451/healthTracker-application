package org.agileSoftDev.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime




object  Activities : Table("Activities") {
    val id = integer("id").autoIncrement()
    var description = varchar("description",100)
    var duration = double("duration")
    var calories = integer("calories")
    val started = datetime( "started")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

