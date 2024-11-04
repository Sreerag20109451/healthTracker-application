package org.agileSoftDev.domain.repository

import org.agileSoftDev.domain.Activity
import org.agileSoftDev.domain.db.Activities
import org.agileSoftDev.utills.mapToActivities
import org.agileSoftDev.utills.mapToUser
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class ActivityDAO {

    private var activities: ArrayList<Activity> = ArrayList()
    fun getAllActivities() : ArrayList<Activity> {

        var activities : ArrayList<Activity> = ArrayList()
        transaction {
            Activities.selectAll().map {
                activities.add(mapToActivities(it))
            }
        }
        return activities
    }

    fun getActivityById(id: Int) : Activity? {
        var activity : Activity? = null
       transaction {
           Activities.selectAll().where { Activities.id eq id }.map {
               activity = mapToActivities(it) }
       }
        return  activity
    }

    fun getActivityByUser(userId: Int) : ArrayList<Activity> {
       var activitiesByUser =ArrayList<Activity>()
     transaction {
         Activities.selectAll().where { Activities.userId eq userId }.map {
             activitiesByUser.add(mapToActivities(it))
         }
     }
        return activitiesByUser
    }

    fun saveActivity(activity: Activity) {
        transaction {
            Activities.insert {
                it[id] = activity.id
                it[description] = activity.description
                it[duration] = activity.duration
                it[calories] = activity.calories
                it[started] = activity.started
                it[userId] = activity.userId
            }
        }
    }

    fun deleteActivityByUser(id: Int, actId: Int){
        transaction {

            Activities.deleteWhere { (Activities.userId eq id) and ( Activities.id eq actId) }

        }
    }


}