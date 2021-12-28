package com.reephub.praeter.data.model.classes

import kotlinx.serialization.Serializable

@Serializable
data class Classes(
    val id: String,
    var name: String,
    var type: String,
    var duration: String,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)
