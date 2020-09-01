package com.ptzkg.erestaurant.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: Int,
    val ID_NO: String,
    val name: String,
    val email: String,
    val password: String,
    val role: String,
    val created_at: String,
    val updated_at: String
): Parcelable