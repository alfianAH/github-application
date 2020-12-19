package com.dicoding.picodiploma.githubapplication.entity

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
    val repositories: Int? = null,
    val followers: Int? = null,
    val following: Int? = null,
    val followersUrl: String? = null,
    val followingUrl: String? = null
): Parcelable