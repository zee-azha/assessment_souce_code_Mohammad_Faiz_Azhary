package com.example.assessment_souce_code_faiz.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM cached_users WHERE login LIKE '%' || :query || '%'")
    fun searchCachedUsers(query: String): Flow<List<UserEntity>>

    @Query("SELECT * FROM cached_users WHERE login = :username LIMIT 1")
    fun getCachedUser(username: String): Flow<UserEntity?>

    @Query("DELETE FROM cached_users")
    suspend fun clearAll()
}
