package com.dicoding.picodiploma.mynoteapps.database

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// TODO : [1] Create Entity
@Parcelize
data class Note(
    var id: Int = 0,
    var title: String? = null,
    var description: String? = null,
    var date: String? = null
) : Parcelable