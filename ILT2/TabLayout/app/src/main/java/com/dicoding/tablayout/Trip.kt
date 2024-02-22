package com.dicoding.tablayout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Trip(
    val id: Int,
    val name: String,
    val description: String,
    val image: Int,
    val price: Int,
    val place: String
): Parcelable
