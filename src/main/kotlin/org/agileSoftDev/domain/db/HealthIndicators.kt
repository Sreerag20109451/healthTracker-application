package org.agileSoftDev.domain.db

import org.agileSoftDev.domain.db.Activities.id
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table


    object HealthIndicators : Table("HealthIndicators") {

        val indicatorid = integer("indicatorid").autoIncrement()
        val userid = integer("userid").references(Users.id,onDelete = ReferenceOption.CASCADE)
        var age = integer("age")
        var height = integer("height").nullable()
        var weight = integer("weight").nullable()
        var boxygen = integer("boxygen").nullable()
        var hdl = integer("hdl").nullable()
        var ldl = integer("ldl").nullable()
        var alt = integer("alt").nullable()
        var ast =  integer("ast").nullable()
        var gfr = integer("gfr").nullable()

        override val primaryKey: PrimaryKey = PrimaryKey(indicatorid)

    }
