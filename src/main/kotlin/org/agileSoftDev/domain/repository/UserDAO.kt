package org.agileSoftDev.domain.repository

import org.agileSoftDev.domain.User
import org.agileSoftDev.domain.db.Users
import org.agileSoftDev.utills.Bcryptutils
import org.agileSoftDev.utills.mapToUser
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.postgresql.util.PSQLException


class UserDAO {




    private val BcryptObj = Bcryptutils()
    fun verifyPassword(user: User, password: String): Boolean {
        return BcryptObj.verifyPassword(password,user.password)
    }
    fun allUsers(): ArrayList<User> {
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
    fun loginUser(email: String, password: String):User? {
        var user :User? = null
        transaction {
            Users.selectAll().where{Users.email eq email}.map{
                user= mapToUser(it)
            }
        }
        if(user == null){
         return  user
     }
        else{
          val isPasswordOk =   verifyPassword(user!!,password)
            if(isPasswordOk) return  user
            else return  null
     }
    }
    fun addUser(user: User){

        var hashedPassword = BcryptObj.hashPassword(user.password)
        transaction {
            Users.insert {
                it[name] = user.name
                it[email] = user.email
                it[password] = hashedPassword
                it[role] = user.role
            }


        }
    }

    fun findByEmail(email: String): User? {
        var user : User? = null
        transaction {
            Users.selectAll().where { Users.email eq email }.map {
                user = mapToUser(it)
            }
        }
        return user
    }

    fun updateUser(id: Int,user: User)  {
        transaction {
                Users.update({ Users.id eq id }) {
                    it[name] = user.name
                    it[email] = user.email
                }
            }
    }

    fun deleteUserById(id: Int){

        transaction {
            Users.deleteWhere{Users.id eq id}
        }


    }


}