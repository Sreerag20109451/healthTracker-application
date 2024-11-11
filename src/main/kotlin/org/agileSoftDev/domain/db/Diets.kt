package org.agileSoftDev.domain.db

import org.jetbrains.exposed.sql.Table

object Diets : Table("diets") {

    val dietid = integer("dietid").autoIncrement()
    val dietname = varchar("dietname",70)
    val content = varchar("content",500)

    override val primaryKey: PrimaryKey = PrimaryKey(dietid)

}