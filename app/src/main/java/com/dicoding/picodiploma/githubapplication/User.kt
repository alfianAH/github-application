package com.dicoding.picodiploma.githubapplication

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    val photo: String,
    val username: String,
    val name: String? = null,
    val urlProfile: String,
    val location: String? = null,
    val company: String? = null,
    val repositories: String? = null,
    val followers: String? = null,
    val following: String? = null
): Parcelable