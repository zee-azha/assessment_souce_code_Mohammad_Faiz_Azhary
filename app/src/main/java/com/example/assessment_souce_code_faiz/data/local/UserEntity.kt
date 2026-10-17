package com.example.assessment_souce_code_faiz.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_users")
data class UserEntity(
    @PrimaryKey val login: String,
    val avatarUrl: String,
    val name: String? = null,
    val company: String? = null,
    val location: String? = null,
    val bio: String? = null,
    val publicRepos: Int = 0,
    val followers: Int = 0,
    val following: Int = 0,
    val updatedAt: Long = System.currentTimeMillis()
)
