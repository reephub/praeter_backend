package com.reephub.praeter.data.model.login

import com.reephub.praeter.data.model.user.User
import com.reephub.praeter.data.model.user.convertToSHA1
import com.reephub.praeter.data.model.user.encodedHashedPassword
import com.reephub.praeter.data.model.user.users
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put


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

        // Convert to SHA-1
        val sha1hash: ByteArray? = convertToSHA1(user.password!!)

        if (null == sha1hash) {
            println("Failed to convert to SHA-1")
            return@post
        }

        // Encode hashed password
        val token: String? = encodedHashedPassword(sha1hash)

        println("generated token : $token \n")

        if (null == token) {
            // TODO : Return user object with token
            call.respond(HttpStatusCode.NotFound, buildJsonObject { put("message", "password is incorrect") })
        } else {

            if (null != user.token && token == users.find { it.email == user.email }!!.token) {

                // User logging is okay
                println("User logging is okay")

                // TODO : Return user object with token
                call.respond(HttpStatusCode.OK, buildJsonObject { put("message", "Login okay") })
            }
        }
    }
}