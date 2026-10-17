package com.example.assessment_souce_code_faiz.domain.repository

import com.example.assessment_souce_code_faiz.domain.model.Repository
import com.example.assessment_souce_code_faiz.domain.model.User

interface UserRepository {
    suspend fun searchUsers(query: String): Result<List<User>>
    suspend fun getUserDetail(username: String): Result<User>
    suspend fun getUserRepos(username: String): Result<List<Repository>>
    suspend fun getFollowers(username: String): Result<List<User>>
    suspend fun getFollowing(username: String): Result<List<User>>
}
