package org.agileSoftDev.domain.db

import org.agileSoftDev.domain.HealthRisk
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object HealthRisks: Table("HealthRisks") {

    val riskid = integer("riskid").autoIncrement()
    val risk = varchar("risk",50)
    val bmi = double("bmi").nullable()
    val boxygen = integer("boxygen").nullable()
    val hdl = integer("hdl").nullable()
    var ldl = integer("ldl").nullable()
    var alt = integer("alt").nullable()
    var ast = integer("ast").nullable()
    var gfr = integer("gfr").nullable()
    var dept = varchar("dept",50).references(HealthDepartments.dept, onDelete = ReferenceOption.CASCADE)
    var dietid = integer("dietid").references(Diets.dietid, onDelete = ReferenceOption.CASCADE).nullable()


    override val primaryKey: PrimaryKey = PrimaryKey(riskid)


}

