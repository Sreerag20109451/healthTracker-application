package org.agileSoftDev.domain.repository

import org.agileSoftDev.config.DBConfig
import org.agileSoftDev.domain.HealthIndicator
import org.agileSoftDev.domain.db.HealthIndicators
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class HealthIndicatorDAO {

    var healthIndicatorFeeder : ArrayList<HealthIndicator> = arrayListOf(
        HealthIndicator(indicatorid = 1, userid = 1, age = 17, height = 178, weight = 67, boxygen = 97, hdl = 40, ldl = 121, ast = 36, alt = 36, gfr = 67  )
    )


    fun addHealthIndicators(healthIndicators: ArrayList<HealthIndicator>) {

        healthIndicators.map{
            hindicator ->
            transaction {
                HealthIndicators.insert {
                it[indicatorid] = hindicator.indicatorid
                    it[userid] =hindicator.userid
                    it[age] = hindicator.age
                    it[height] = hindicator.height
                    it[weight] = hindicator.weight
                    it[boxygen] = hindicator.boxygen
                    it[hdl] = hindicator.hdl
                    it[ldl] = hindicator.ldl
                    it[alt] =hindicator.alt
                    it[ast] =hindicator.ast
                    it[gfr] =hindicator.gfr
                }
            }

        }


    }


}

fun main(){



    DBConfig().getConnection()
    val healthIndicatorDAO = HealthIndicatorDAO()
    healthIndicatorDAO.addHealthIndicators(healthIndicatorDAO.healthIndicatorFeeder)
}