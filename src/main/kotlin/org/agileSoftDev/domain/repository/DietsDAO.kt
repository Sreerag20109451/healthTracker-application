package org.agileSoftDev.domain.repository

import org.agileSoftDev.domain.Diet
import org.agileSoftDev.domain.db.Diets
import org.agileSoftDev.domain.db.HealthRisks
import org.agileSoftDev.utills.mapToDiet
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class DietsDAO {

    fun getDiet(risks: ArrayList<String>?) : List<Diet>? {
        if(risks == null) return null
        if(risks.isEmpty()) return null
        var dietIds: ArrayList<Int?> = arrayListOf()
        var diet: ArrayList<Diet> = arrayListOf()
        risks.forEach { risk ->

            transaction {
                HealthRisks.selectAll().where { HealthRisks.risk eq risk }.map {
                    var dietid: Int? = it[HealthRisks.dietid]?.toInt()
                    Diets.selectAll().where { Diets.dietid eq dietid!! }.map {
                        diet.add(mapToDiet(it))
                    }
                }
            }
        }
        return diet.distinct()
    }
}