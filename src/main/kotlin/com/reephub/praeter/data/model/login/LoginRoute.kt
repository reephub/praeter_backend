package com.reephub.praeter.data.model.login

import com.reephub.praeter.data.model.api.ApiResponse
import com.reephub.praeter.data.model.user.User
import com.reephub.praeter.data.model.user.users
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*


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
            return@post call.respond(
                HttpStatusCode.BadRequest,
                ApiResponse(
                    "Email or password is empty. Please verify that you've entered a correct email or password",
                    HttpStatusCode.BadRequest.value
                )
            )
        }

        if (null == users.find { it.email == user.email }) {
            return@post call.respond(
                HttpStatusCode.NotFound,
                ApiResponse("No user found with this name ${user.email}", HttpStatusCode.NotFound.value)
            )
        }

        if (null == user.token) {
            call.respond(
                HttpStatusCode.NotFound,
                ApiResponse("password is incorrect", HttpStatusCode.NotFound.value)
            )
        } else {

            if (user.token == users.find { it.email == user.email }!!.token) {

                // User logging is okay
                println("User logging is okay")

                call.respond(
                    HttpStatusCode.OK,
                    ApiResponse(
                        "Login okay",
                        HttpStatusCode.OK.value,
                        users.find { it.email == user.email }!!.token
                    )
                )
            }
        }
    }
}