package com.ptzkg.erestaurant.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    val id: Int,
    val voucher_no: String,
    val user_id: User,
    val table: Table,
    val total_price: String,
    val created_at: String,
    val updated_at: String
): Parcelable