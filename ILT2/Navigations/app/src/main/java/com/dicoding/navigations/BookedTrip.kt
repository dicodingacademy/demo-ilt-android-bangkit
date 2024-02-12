package com.dicoding.navigations

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookedTrip(
    val tripID: Int,
    val tripDate: String
): Parcelable
