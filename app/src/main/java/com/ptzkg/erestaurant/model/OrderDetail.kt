package com.ptzkg.erestaurant.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderDetail(
    val id: String,
    val voucher_no: String,
    val menu: Menu,
    val qty: String,
    val total_price: String,
    val status: String,
    val user_id: User,
    val created_at: String,
    val updated_at: String
): Parcelable