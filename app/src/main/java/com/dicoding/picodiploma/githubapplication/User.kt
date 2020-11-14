package com.dicoding.picodiploma.githubapplication

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    val photo: String,
    val username: String,
    val name: String,
    val location: String,
    val company: String,
    val repositories: String,
    val followers: String,
    val following: String
): Parcelable