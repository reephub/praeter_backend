package com.reephub.praeter.data.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(val code: Int, val message: String, val token: String)
