package com.reephub.praeter.ancient

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

val ancientStorage = mutableListOf<Ancient>(
    Ancient("0", "Chez Saint-Honoré", 48.8746168, 2.4634656),
    Ancient("1", "La Case d'Eugénie", 48.8752278, 2.4618618)
)

////////// Declarations /////////////
fun Route.ancientRouting() {
    route("/ancient") {
        get {
            if (ancientStorage.isNotEmpty()) {
                call.respond(ancientStorage)
            } else {
                call.respondText("No Ancients found", status = HttpStatusCode.NotFound)
            }
        }
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            val ancient =
                ancientStorage.find { it.id == id } ?: return@get call.respondText(
                    "No Ancient with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(ancient)
        }
        post {
            val ancient = call.receive<Ancient>()
            ancientStorage.add(ancient)
            call.respondText("Ancient stored correctly", status = HttpStatusCode.Created)
        }
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (ancientStorage.removeIf { it.id == id }) {
                call.respondText("Ancient removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }

        }
    }
}

////////// Register /////////////
fun Application.registerAncientRoutes() {
    routing {
        ancientRouting()
    }
}
