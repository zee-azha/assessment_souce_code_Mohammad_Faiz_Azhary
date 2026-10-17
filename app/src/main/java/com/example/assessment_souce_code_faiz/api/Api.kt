package com.example.assessment_souce_code_faiz.api

import com.example.assessment_souce_code_faiz.data.RepoItem
import com.example.assessment_souce_code_faiz.data.UserResponse
import com.example.assessment_souce_code_faiz.data.UsersDetails
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("search/users")
    suspend fun getOnSearch(
        @Query("q") query: String
    ): UserResponse

    @GET("users/{username}")
    suspend fun getUsersDetails(
        @Path("username") userName: String
    ): UsersDetails

    @GET("users/{username}/repos")
    suspend fun getUserRepos(
        @Path("username") userName: String
    ): List<RepoItem>

    @GET("users/{username}/followers")
    suspend fun getUsersFollowers(
        @Path("username") userName: String
    ): List<UsersDetails>

    @GET("users/{username}/following")
    suspend fun getUsersFollowing(
        @Path("username") userName: String
    ): List<UsersDetails>
}
