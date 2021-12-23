package com.reephub.praeter.data.dao

import com.reephub.praeter.user.User
import com.reephub.praeter.user.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.Closeable

class UsersDao(val db:Database) : DAOInterface{
    override fun init() = transaction(db){
        addLogger(StdOutSqlLogger)

        SchemaUtils.create(Users)
    }

    override fun createUser(title: String, description: String)= transaction(db){
        Users.insert {
            it[Users.firstName] = title
            it[Users.lastName] = description
        }
        Unit
    }

    override fun updateUser(id: Int, title: String, description: String)= transaction(db){
        Users.update({ Users.id eq id.toString() }) {
            it[Users.firstName] = title
            it[Users.firstName] = description
        }
        Unit
    }

    override fun deleteUser(id: Int)  = transaction(db){
        Users.deleteWhere { Users.id eq id.toString() }
        Unit
    }


    override fun getUser(id: Int): User? = transaction(db) {
        Users.select { Users.id eq id.toString() }.map {
            User(
                it[Users.id].toInt(), it[Users.firstName], it[Users.lastName]
            )
        }.singleOrNull()
    }

    override fun getAllUsers(): List<User> = transaction(db) {
        Users.selectAll().map {
            User(
                it[Users.id].toInt(), it[Users.firstName], it[Users.lastName]
            )
        }
    }

    override fun close() {
        // Ignored
    }
}