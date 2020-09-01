package com.ptzkg.erestaurant.model

data class OrderJson (
    val menu_id: Int,
    val qty: Int,
    val total: Double
)

data class OrderDetailJson (
    val menu_id: String,
    val qty: String
)