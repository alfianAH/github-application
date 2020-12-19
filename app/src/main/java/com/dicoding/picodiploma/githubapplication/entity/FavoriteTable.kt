package com.dicoding.picodiploma.githubapplication.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoriteTable (
    var id: Int = 0,
    var username: String
): Parcelable