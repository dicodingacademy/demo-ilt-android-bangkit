package com.dicoding.navigations

import android.app.Application

class MyApplication: Application() {
    companion object{
        val userData = UserAccount(
            balance = 1000,
            listOfBookTrip = mutableListOf<BookedTrip>())
    }
}