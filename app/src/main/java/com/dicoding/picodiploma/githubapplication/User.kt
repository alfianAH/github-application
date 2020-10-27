package com.dicoding.picodiploma.githubapplication

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    var photo: Int,
    var username: String,
    var name: String,
    var location: String,
    var company: String,
    var repositories: String,
    var followers: String,
    var following: String
): Parcelable