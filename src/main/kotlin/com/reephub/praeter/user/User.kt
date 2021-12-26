package com.reephub.praeter.user

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table


object Users : Table() {
    val id = varchar("id", 10) // Column<String>
    val gender = varchar("gender", 10) // Column<String>
    val firstName = varchar("firstName", length = 50) // Column<String>
    val lastName = varchar("lastName", length = 50) // Column<String>
    val email = varchar("email", length = 50) // Column<String>
    val password = varchar("password", length = 50) // Column<String>
    val phoneNumber = varchar("phoneNumber", length = 13) // Column<String>
    val dateOfBirth = varchar("dateOfBirth", length = 50) // Column<String>
    val isPremium = bool("isPremium") // Column<Boolean>
    val isCustomer = bool("isCustomer") // Column<Boolean>
    val isProvider = bool("isProvider") // Column<Boolean>

    override val primaryKey = PrimaryKey(id, name = "PK_User_ID") // name is optional here
}

@Serializable
data class User(
    var gender: String?,
    var firstName: String?,
    var lastName: String?,
    var email: String?,
    var password: String?,
    var phoneNumber: String?,
    var dateOfBirth: String?,
    var isPremium: Boolean = false,
    var isCustomer: Boolean = false,
    var isProvider: Boolean = false
) {
    var id: Int = 0
    var token: String = ""

    constructor(id: Int, firstName: String, lastName: String) : this(
        firstName,
        lastName,
        null,
        null,
        null,
        null,
        null,
    ) {
        this.id = id
    }

}
