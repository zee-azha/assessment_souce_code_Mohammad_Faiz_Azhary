package com.example.assessment_souce_code_faiz.data.repository

import com.example.assessment_souce_code_faiz.api.Api
import com.example.assessment_souce_code_faiz.data.local.UserDao
import com.example.assessment_souce_code_faiz.data.local.UserEntity
import com.example.assessment_souce_code_faiz.domain.model.Repository
import com.example.assessment_souce_code_faiz.domain.model.User
import com.example.assessment_souce_code_faiz.domain.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val api: Api,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun searchUsers(query: String): Result<List<User>> {
        return try {
            val response = api.getOnSearch(query)
            val users = response.items.map { details ->
                User(
                    login = details.login,
                    avatarUrl = details.avatar_url,
                    name = details.name,
                    company = details.company,
                    location = details.location,
                    bio = details.bio,
                    publicRepos = details.public_repos,
                    followers = details.followers,
                    following = details.following
                )
            }
            val entities = users.map { user ->
                UserEntity(
                    login = user.login,
                    avatarUrl = user.avatarUrl,
                    name = user.name,
                    company = user.company,
                    location = user.location,
                    bio = user.bio,
                    publicRepos = user.publicRepos,
                    followers = user.followers,
                    following = user.following
                )
            }
            userDao.insertUsers(entities)
            Result.success(users)
        } catch (e: Exception) {
            val cachedEntities = userDao.searchCachedUsers(query).firstOrNull() ?: emptyList()
            if (cachedEntities.isNotEmpty()) {
                val cachedUsers = cachedEntities.map { entity ->
                    User(
                        login = entity.login,
                        avatarUrl = entity.avatarUrl,
                        name = entity.name,
                        company = entity.company,
                        location = entity.location,
                        bio = entity.bio,
                        publicRepos = entity.publicRepos,
                        followers = entity.followers,
                        following = entity.following
                    )
                }
                Result.success(cachedUsers)
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun getUserDetail(username: String): Result<User> {
        return try {
            val details = api.getUsersDetails(username)
            val user = User(
                login = details.login,
                avatarUrl = details.avatar_url,
                name = details.name,
                company = details.company,
                location = details.location,
                bio = details.bio,
                publicRepos = details.public_repos,
                followers = details.followers,
                following = details.following
            )
            val entity = UserEntity(
                login = user.login,
                avatarUrl = user.avatarUrl,
                name = user.name,
                company = user.company,
                location = user.location,
                bio = user.bio,
                publicRepos = user.publicRepos,
                followers = user.followers,
                following = user.following
            )
            userDao.insertUser(entity)
            Result.success(user)
        } catch (e: Exception) {
            val cachedEntity = userDao.getCachedUser(username).firstOrNull()
            if (cachedEntity != null) {
                val user = User(
                    login = cachedEntity.login,
                    avatarUrl = cachedEntity.avatarUrl,
                    name = cachedEntity.name,
                    company = cachedEntity.company,
                    location = cachedEntity.location,
                    bio = cachedEntity.bio,
                    publicRepos = cachedEntity.publicRepos,
                    followers = cachedEntity.followers,
                    following = cachedEntity.following
                )
                Result.success(user)
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun getUserRepos(username: String): Result<List<Repository>> {
        return try {
            val repoItems = api.getUserRepos(username)
            val repos = repoItems.map { item ->
                Repository(
                    id = item.id,
                    name = item.name,
                    description = item.description,
                    starCount = item.stargazers_count,
                    forkCount = item.forks_count,
                    language = item.language,
                    htmlUrl = item.html_url
                )
            }
            Result.success(repos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFollowers(username: String): Result<List<User>> {
        return try {
            val followers = api.getUsersFollowers(username)
            val users = followers.map { details ->
                User(
                    login = details.login,
                    avatarUrl = details.avatar_url
                )
            }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFollowing(username: String): Result<List<User>> {
        return try {
            val following = api.getUsersFollowing(username)
            val users = following.map { details ->
                User(
                    login = details.login,
                    avatarUrl = details.avatar_url
                )
            }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
