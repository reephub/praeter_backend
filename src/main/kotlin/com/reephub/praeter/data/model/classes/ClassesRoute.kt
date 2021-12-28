package com.reephub.praeter.data.model.classes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

val classesStorage = mutableListOf(
    Classes("0", "Gwoka", "Dance", "02h00", 48.8752278, 2.4618618),
    Classes("1", "Circuit course", "Sport", "00h30", 48.7572914, 1.9855431),
    Classes("2", "Bibliothèque", "Reading", "03h00", 48.8335877, 2.3735772)
)

////////// Declarations /////////////
fun Route.classesRouting() {
    route("/classes") {
        get {
            if (classesStorage.isNotEmpty()) {
                call.respond(classesStorage)
            } else {
                call.respondText("No classes found", status = HttpStatusCode.NotFound)
            }
        }
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            val classes =
                classesStorage.find { it.id == id } ?: return@get call.respondText(
                    "No classes with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(classes)
        }
        post {
            val classes = call.receive<Classes>()
            classesStorage.add(classes)
            call.respondText("Classes stored correctly", status = HttpStatusCode.Created)
        }
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (classesStorage.removeIf { it.id == id }) {
                call.respondText("Classes removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }

        }
    }
}

////////// Register /////////////
fun Application.registerClassesRoutes() {
    routing {
        classesRouting()
    }
}
