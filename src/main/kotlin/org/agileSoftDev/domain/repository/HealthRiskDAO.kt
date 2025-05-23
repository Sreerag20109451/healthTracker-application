package org.agileSoftDev.domain.repository

import org.agileSoftDev.domain.Diet
import org.agileSoftDev.domain.HealthIndicator
import org.agileSoftDev.domain.db.Diets
import org.agileSoftDev.domain.db.HealthRisks
import org.agileSoftDev.utills.mapToDiet
import org.agileSoftDev.utills.selectDistinct
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class HealthRiskDAO {
    private val healthIndicatorDAO = HealthIndicatorDAO()


    fun getPossibleHealthRisks(userID: Int): ArrayList<String>? {
        var risks: ArrayList<String> = arrayListOf()
        var healthIndicators: HealthIndicator? = healthIndicatorDAO.getHealthIndicatorsByUser(userID)
        if (healthIndicators == null) return null
        var indicatorMap: MutableMap<String, Int> = healthIndicatorDAO.nonNullIndicators(healthIndicators!!)
        var greaterIndicator = arrayListOf("boxygen", "hdl", "gfr")
        indicatorMap.forEach { (key, value) ->
            if (key in greaterIndicator) {
                //The idea for the  structure of the below transaction was obtained while debugging(The block of code generated was incorrect)
                transaction {
                    val query = when (key) {

                        "boxygen" -> HealthRisks.selectAll().where { HealthRisks.boxygen greater  value }
                        "hdl" -> HealthRisks.selectAll().where { HealthRisks.hdl greater value }
                        "gfr" -> HealthRisks.selectAll().where { HealthRisks.gfr greater value }
                        else -> null
                    }
                    query?.map {
                        var risk = it[HealthRisks.risk].toString()
                        risks.add(risk)
                    }
                }
            } else {
                transaction {
                    val query = when (key) {

                        "ldl" -> HealthRisks.selectAll().where { HealthRisks.ldl less value }
                        "alt" -> HealthRisks.selectAll().where { HealthRisks.alt less value }
                        "ast" -> HealthRisks.selectAll().where { HealthRisks.ast less  value }
                        "bmi" -> HealthRisks.selectAll().where { HealthRisks.bmi less  value.toDouble() }
                        else -> null
                    }
                    query?.map {
                        var risk = it[HealthRisks.risk].toString()
                        risks.add(risk)
                    }
                }
            }
        }
        return  selectDistinct(risks)
    }


}