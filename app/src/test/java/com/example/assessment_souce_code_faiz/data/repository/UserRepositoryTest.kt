package com.example.assessment_souce_code_faiz.data.repository

import com.example.assessment_souce_code_faiz.api.Api
import com.example.assessment_souce_code_faiz.data.RepoItem
import com.example.assessment_souce_code_faiz.data.UserResponse
import com.example.assessment_souce_code_faiz.data.UsersDetails
import com.example.assessment_souce_code_faiz.data.local.UserDao
import com.example.assessment_souce_code_faiz.data.local.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {

    private lateinit var repository: UserRepositoryImpl
    private val fakeApi = FakeApi()
    private val fakeDao = FakeUserDao()

    @Before
    fun setUp() {
        repository = UserRepositoryImpl(fakeApi, fakeDao)
    }

    @Test
    fun searchUsers_returnsSuccess() = runTest {
        val result = repository.searchUsers("octocat")
        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals("octocat", result.getOrNull()?.first()?.login)
    }

    private class FakeApi : Api {
        override suspend fun getOnSearch(query: String): UserResponse {
            return UserResponse(
                items = arrayListOf(
                    UsersDetails(login = "octocat", avatar_url = "https://github.com/avatar.png")
                )
            )
        }

        override suspend fun getUsersDetails(userName: String): UsersDetails {
            return UsersDetails(login = userName, avatar_url = "https://github.com/avatar.png")
        }

        override suspend fun getUserRepos(userName: String): List<RepoItem> {
            return emptyList()
        }

        override suspend fun getUsersFollowers(userName: String): List<UsersDetails> {
            return emptyList()
        }

        override suspend fun getUsersFollowing(userName: String): List<UsersDetails> {
            return emptyList()
        }
    }

    private class FakeUserDao : UserDao {
        private val list = mutableListOf<UserEntity>()

        override suspend fun insertUsers(users: List<UserEntity>) {
            list.addAll(users)
        }

        override suspend fun insertUser(user: UserEntity) {
            list.add(user)
        }

        override fun searchCachedUsers(query: String): Flow<List<UserEntity>> {
            return flowOf(list.filter { it.login.contains(query, ignoreCase = true) })
        }

        override fun getCachedUser(username: String): Flow<UserEntity?> {
            return flowOf(list.firstOrNull { it.login.equals(username, ignoreCase = true) })
        }

        override suspend fun clearAll() {
            list.clear()
        }
    }
}

