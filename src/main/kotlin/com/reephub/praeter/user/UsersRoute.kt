package com.reephub.praeter.user

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*


val users = mutableListOf<User>(
    User("Male", "Michael", "Lawrence", "mike@test.fr", "test","+33645789312", "20/08/1993",isPremium = true, isCustomer = true, isProvider = false),
    User("Female", "Jane", "Doe", "janedoe@test.fr", "test","+33645789312", "01/08/1991",isPremium = true, isCustomer = true, isProvider = false),
    User("Male", "John", "Smith", "johnsmith@test.fr", "test","+33645789312", "25/02/1988",isPremium = true, isCustomer = true, isProvider = false)
)


fun Application.registerUsersRoute() {
    routing {
        route("/users") {
            addUserRoute()
            getUsersRoute()
            getUserRoute()
            deleteUserRoute()
        }
    }
}


fun Route.addUserRoute() {
    post {
        val user = call.receive<User>()
        print("received : $user")
        users.add(user)
        call.respond(HttpStatusCode.Created, "User saved")
    }
}

fun Route.getUsersRoute() {
    get {
        if (users.isNotEmpty()) {
            call.respond(HttpStatusCode.OK, users)
        } else {
            call.respond(HttpStatusCode.NotFound, "No user found")
        }
    }
}

fun Route.getUserRoute() {
    get("{id}") {
        val id = call.parameters["id"]?.toInt() ?: return@get call.respondText(
            "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )
        val user = users.find { it.id == id }
        user?.let { call.respond(HttpStatusCode.Found, it) } ?: return@get call.respond(
            HttpStatusCode.NotFound,
            "No user found with this id $id"
        )
    }
}

fun Route.deleteUserRoute() {
    delete("{id}") {
        val id =
            call.parameters["id"]?.toInt() ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                "User id required"
            )

        if (users.removeIf { it.id == id }) {
            call.respond(HttpStatusCode.Accepted, "User removed successfully")
        } else {
            call.respond(HttpStatusCode.NotFound, "No user found with this id $id")
        }
    }
}
