package com.reephub.praeter.data.model.login

import com.reephub.praeter.user.User
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.json.*


val users = mutableListOf<User>(
    User(
        "Male",
        "Michael",
        "Lawrence",
        "mike@test.fr",
        "test",
        "+33645789312",
        "20/08/1993",
        isPremium = true,
        isCustomer = true,
        isProvider = false
    ),
    User(
        "Female",
        "Jane",
        "Doe",
        "janedoe@test.fr",
        "test",
        "+33645789312",
        "01/08/1991",
        isPremium = true,
        isCustomer = true,
        isProvider = false
    ),
    User(
        "Male",
        "John",
        "Smith",
        "johnsmith@test.fr",
        "test",
        "+33645789312",
        "25/02/1988",
        isPremium = true,
        isCustomer = true,
        isProvider = false
    )
)


fun Application.registerLoginRoute() {
    routing {
        route("/login") {
            loginRoute()
        }
    }
}


fun Route.loginRoute() {
    post {
        val user = call.receive<User>()
        print("received : $user")

        if (user.email?.isBlank() == true || user.password?.isBlank() == true) {
            return@post call.respondText(
                "Email or password is empty. Please verify that you've entered a correct email or password",
                status = HttpStatusCode.BadRequest
            )
        }

        if (null == users.find { it.email == user.email }) {
            return@post call.respond(
                HttpStatusCode.NotFound,
                "No user found with this name ${user.email}"
            )
        }

        // User logging is okay
        println("User logging is okay")

        call.respond(HttpStatusCode.OK,  "{ \"message\": \"Login okay\"}")
    }
}