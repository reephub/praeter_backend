package com.reephub.praeter.data.model.ancient

import kotlinx.serialization.Serializable

@Serializable
data class Ancient(
    val id: String,
    var name: String? = null,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)
