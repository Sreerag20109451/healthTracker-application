package org.agileSoftDev.domain.repository

import org.agileSoftDev.domain.HealthIndicator
import org.agileSoftDev.domain.db.HealthIndicators
import org.agileSoftDev.utills.healthIndicators.HealthIndexes
import org.agileSoftDev.utills.mapToHealthIndicator
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class HealthIndicatorDAO {
    private val userDAO = UserDAO()
    private val healthIndexes =HealthIndexes()

    var healthIndicatorFeeder : ArrayList<HealthIndicator> = arrayListOf(
        HealthIndicator(indicatorid = 1, userid = 1, age = 17, height = 178, weight = 67, boxygen = 97, hdl = 40, ldl = 121, ast = 36, alt = 36, gfr = 67  )
    )

    fun addHealthIndicatorByUser(userId: Int, healthIndicator: HealthIndicator){

        transaction {
            HealthIndicators.insert {
                it[userid] = userId
                it[age] = healthIndicator.age
                it[height] = healthIndicator.height
                it[weight] = healthIndicator.weight
                it[boxygen] = healthIndicator.boxygen
                it[hdl] = healthIndicator.hdl
                it[ldl] = healthIndicator.ldl
                it[alt] = healthIndicator.alt
                it[ast] = healthIndicator.ast
                it[gfr] = healthIndicator.gfr
            }
        }

    }

    fun getHealthIndicatorsByUser(userId: Int): HealthIndicator? {
        val user = userDAO.getUserById(userId)
        var healthIndicators: HealthIndicator? = null
        if(user == null) return null
        else{
            transaction {

                HealthIndicators.selectAll().where { HealthIndicators.userid eq userId }.map {

                    healthIndicators = mapToHealthIndicator(it)
                }
            }
            return healthIndicators
        }
    }

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

    fun nonNullIndicators(healthIndicator: HealthIndicator) : MutableMap<String,Int>{

        var mapUser : MutableMap<String, Int > = mutableMapOf()
        if(healthIndicator.hdl != null) mapUser.set("hdl", healthIndicator.hdl!!)
        if (healthIndicator.ldl != null) mapUser.set("ldl", healthIndicator.ldl!!)
        if(healthIndicator.alt != null) mapUser.set("alt", healthIndicator.alt!!)
        if(healthIndicator.ast != null) mapUser.set("ast", healthIndicator.ast!!)
        if(healthIndicator.gfr != null) mapUser.set("gfr", healthIndicator.gfr!!)

        return mapUser



    }



}