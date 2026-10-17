package com.example.assessment_souce_code_faiz.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UsersDetails(
    val login: String = "",
    val company: String? = null,
    val location: String? = null,
    val avatar_url: String = "",
    val public_repos: Int = 0,
    val name: String? = null,
    val followers: Int = 0,
    val following: Int = 0,
    val bio: String? = null,
    val email: String? = null,
    val blog: String? = null,
    val twitter_username: String? = null
) : Parcelable
