package com.reephub.praeter

import com.reephub.praeter.data.model.ancient.registerAncientRoutes
import com.reephub.praeter.data.model.classes.registerClassesRoutes
import com.reephub.praeter.data.dao.UsersDao
import com.reephub.praeter.data.model.login.registerLoginRoute
import com.reephub.praeter.data.model.order.registerOrderRoutes
import com.reephub.praeter.data.model.user.Users
import com.reephub.praeter.data.model.user.registerUsersRoute
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>): Unit {
//    initDatabase()

    /*val dao = UsersDao(Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver"))
    dao.init()*/

    io.ktor.server.netty.EngineMain.main(args)
}

fun initDatabase() {
    Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")

    transaction {
        addLogger(StdOutSqlLogger)

        SchemaUtils.create(Users)
        Users.insert {
            it[id] = "andrey"
            it[firstName] = "Andrey"
        }

        Users.insert {
            it[id] = "sergey"
            it[firstName] = "Sergey"
        }

        Users.insert {
            it[id] = "eugene"
            it[firstName] = "Eugene"
        }

        Users.insert {
            it[id] = "alex"
            it[firstName] = "Alex"
        }

        Users.insert {
            it[id] = "smth"
            it[firstName] = "Something"
        }

        Users.update({ Users.id eq "alex" }) {
            it[firstName] = "Alexey"
        }

        Users.deleteWhere { Users.firstName like "%thing" }

        println("All users:")
        for (user in Users.selectAll()) {
            println("${user[Users.id]}: ${user[Users.firstName]}")
        }
    }
}

@Suppress("unused") // Referenced in application.conf
@JvmOverloads
fun Application.module(testing: Boolean = false) {
    val port = environment.config.propertyOrNull("ktor.deployment.port")?.getString() ?: "8080"

    println("Listening on port $port")

    embeddedServer(Netty, host = "192.168.0.48", port = 8100) {
//    embeddedServer(Netty, host = "192.168.0.136", port = 8100) {

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }

        registerLoginRoute()
        registerUsersRoute()
        registerOrderRoutes()
        registerClassesRoutes()
        registerAncientRoutes()


        routing {

            get("") {
                call.respondText("Hello, world!", contentType = ContentType.Text.Plain)
            }
            get("/checkout") {
                call.respondText("checkout - list users checkout products", contentType = ContentType.Text.Plain)
            }
            get("/about") {
                call.respondText("About - Author MichaelStH", contentType = ContentType.Text.Plain)
            }
        }
    }.start(wait = true)
}
