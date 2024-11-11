package org.agileSoftDev.domain.db

import org.jetbrains.exposed.sql.Table

object HealthDepartments: Table("healthdepartments") {

    val deptid = integer("deptid").autoIncrement()
    val dept = varchar("dept", 50)

    override val primaryKey : PrimaryKey = PrimaryKey(deptid)

}