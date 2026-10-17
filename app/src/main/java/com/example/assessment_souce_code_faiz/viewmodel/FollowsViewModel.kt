package com.example.assessment_souce_code_faiz.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assessment_souce_code_faiz.data.UsersDetails
import com.example.assessment_souce_code_faiz.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _followersList = MutableLiveData<List<UsersDetails>>()
    val followersList: LiveData<List<UsersDetails>> = _followersList

    private val _followingList = MutableLiveData<List<UsersDetails>>()
    val followingList: LiveData<List<UsersDetails>> = _followingList

    private val _isLoadingFollowers = MutableStateFlow(false)
    val isLoadingFollowers: StateFlow<Boolean> = _isLoadingFollowers

    private val _isLoadingFollowing = MutableStateFlow(false)
    val isLoadingFollowing: StateFlow<Boolean> = _isLoadingFollowing

    fun setListFollowers(username: String) {
        if (username.isBlank()) return
        viewModelScope.launch {
            _isLoadingFollowers.value = true
            val result = userRepository.getFollowers(username)
            result.onSuccess { users ->
                _followersList.value = users.map { user ->
                    UsersDetails(
                        login = user.login,
                        avatar_url = user.avatarUrl
                    )
                }
            }.onFailure {
                _followersList.value = emptyList()
            }
            _isLoadingFollowers.value = false
        }
    }

    fun getListFollowers(): LiveData<List<UsersDetails>> = _followersList

    fun setListFollowing(username: String) {
        if (username.isBlank()) return
        viewModelScope.launch {
            _isLoadingFollowing.value = true
            val result = userRepository.getFollowing(username)
            result.onSuccess { users ->
                _followingList.value = users.map { user ->
                    UsersDetails(
                        login = user.login,
                        avatar_url = user.avatarUrl
                    )
                }
            }.onFailure {
                _followingList.value = emptyList()
            }
            _isLoadingFollowing.value = false
        }
    }

    fun getListFollowing(): LiveData<List<UsersDetails>> = _followingList
}
