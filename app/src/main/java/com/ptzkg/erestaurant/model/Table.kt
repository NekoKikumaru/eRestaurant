package com.ptzkg.erestaurant.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Table(
    val id: Int,
    val table_no: String,
    val status: String,
    val created_at: String,
    val updated_at: String
): Parcelable