package com.ptzkg.erestaurant.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SubCategory(
    val id: Int,
    val name: String,
    val category: Category,
    val created_at: String,
    val updated_at: String,
    var expanded: Boolean = false
): Parcelable