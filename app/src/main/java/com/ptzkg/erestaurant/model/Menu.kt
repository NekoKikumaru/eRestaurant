package com.ptzkg.erestaurant.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Menu(
    val id: Int,
    val name: String,
    val subCategory: SubCategory,
    val price: String,
    val image: String,
    val created_at: String,
    val updated_at: String
): Parcelable