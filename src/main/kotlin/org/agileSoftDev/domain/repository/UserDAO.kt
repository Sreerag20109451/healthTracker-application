package org.agileSoftDev.domain.repository

import org.agileSoftDev.domain.User
import org.agileSoftDev.domain.db.Users
import org.agileSoftDev.utills.mapToUser
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction


class UserDAO {


    fun allUsers(): ArrayList<User>? {

        val userList: ArrayList<User> = arrayListOf()
        transaction {
            Users.selectAll().map {

                userList.add(mapToUser(it)) }
        }
        return userList
    }

    fun getUserById(id: Int): User? {
      var user : User? = null

      transaction {

          Users.selectAll().where{Users.id eq id}.map{


              user = mapToUser(it)

          }
      }
        return  user

    }

    fun addUser(user: User){


    }

    fun findByEmail(email: String?): User? {

        return null
    }

    fun updateUser(id: Int,user: User)  {


    }

    fun deleteUserById(id: Int){


    }


}