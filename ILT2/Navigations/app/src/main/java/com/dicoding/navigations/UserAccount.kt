package com.dicoding.navigations

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserAccount(
    var balance: Int,
    var listOfBookTrip: MutableList<BookedTrip>
): Parcelable
