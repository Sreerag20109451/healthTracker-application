package org.agileSoftDev.domain.repository

import org.agileSoftDev.domain.User


class UserDAO {
    private val users = arrayListOf<User>(
        User(name = "Alice", email = "alice@wonderland.com", id = 0),
        User(name = "Bob", email = "bob@cat.ie", id = 1),
        User(name = "Mary", email = "mary@contrary.com", id = 2),
        User(name = "Carol", email = "carol@singer.com", id = 3)
    )

    fun allUsers(): ArrayList<User> {

        return users


    }

    fun getUserById(id: Int): User? {

        return users.find { it.id == id }

    }

    fun addUser(user: User){

        users.add(user)
    }

    fun findByEmail(email: String?): User? {

        return users.find { it.email == email }
    }

    fun updateUser(id: Int,user: User) : ArrayList<User>? {

        val userFound = users.find { it.id == id }

        if(user != null){

            users[users.indexOf(userFound)] = user
            return users

        }
        return null

    }


}