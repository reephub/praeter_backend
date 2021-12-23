package com.reephub.praeter.order

import kotlinx.serialization.Serializable

@Serializable
data class OrderItem(val item: String, val amount: Int, val price: Double)