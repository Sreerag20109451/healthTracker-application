package org.agileSoftDev.utills.Healthrisks

import org.agileSoftDev.config.DBConfig
import org.agileSoftDev.domain.Diet
import org.agileSoftDev.domain.HealthDepartment
import org.agileSoftDev.domain.HealthRisk
import org.agileSoftDev.domain.db.Diets
import org.agileSoftDev.domain.db.HealthDepartments
import org.agileSoftDev.domain.db.HealthRisks
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

fun healthriskAdd(healthRisk: HealthRisk){
    transaction {
        HealthRisks.insert {
            it[risk] = healthRisk.risk
            it[bmi] = healthRisk.bmi
            it[hdl] = healthRisk.hdl
            it[hdl]=healthRisk.hdl
            it[ldl] = healthRisk.ldl
            it[alt]= healthRisk.alt
            it[ast]=healthRisk.ast
            it[gfr] = healthRisk.gfr
            it[dept] = healthRisk.dept
            it[dietid] = healthRisk.dietid
        }
    }


}

fun DietsAdd(diet: Diet){

    transaction {
        Diets.insert {
            it[dietname] = diet.dietname
            it[content] = diet.content
        }
    }

}

fun deptAdd (department: HealthDepartment){

    transaction {
        HealthDepartments.insert {
            it[dept] = department.dept
        }
    }

}
fun main() {

    DBConfig().getConnection()
//
    val Obesity = HealthRisk(riskid = 1, risk = "Obesity", bmi = 30.00, boxygen = null, hdl = 50, ldl = 106, alt = 40 , ast = 40, gfr = null, dept = "Bariatrics", dietid = 1 )
    val HeartDiseases = HealthRisk(riskid = 2, risk = "Heart Diseases", boxygen = null, bmi = 25.5, hdl = 59, ldl = 100, alt = 36, ast = 36, gfr =null, dept = "Cardiology", dietid = 1 )
//    val Bariatrics = HealthDepartment(deptid = 1, dept = "Bariatrics")
//    val Cardiology = HealthDepartment(deptid = 2, dept = "Cardiology")
//    val Nephrology = HealthDepartment(deptid = 3, dept = "Nephrology")
//    val Gastroenterology = HealthDepartment(deptid = 4, dept = "Gastroenterology")
////
//    val lowFatdiet = Diet(1, "Low fat diet", content = "Fruits, Vegetables, Fibres, Nuts")


//    deptAdd(Bariatrics)
//    deptAdd(Cardiology)
//    deptAdd(Nephrology)
//    deptAdd(Gastroenterology)





}