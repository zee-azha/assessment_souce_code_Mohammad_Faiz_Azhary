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
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userList = MutableLiveData<List<UsersDetails>>()
    val userList: LiveData<List<UsersDetails>> = _userList

    private val _isLoading = MutableStateFlow(false)
    val isLoadingState: StateFlow<Boolean> = _isLoading

    fun setOnSearch(query: String) {
        if (query.isBlank()) {
            _userList.value = emptyList()
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            val result = userRepository.searchUsers(query)
            result.onSuccess { users ->
                _userList.value = users.map { user ->
                    UsersDetails(
                        login = user.login,
                        avatar_url = user.avatarUrl,
                        name = user.name ?: "",
                        company = user.company ?: "",
                        location = user.location ?: "",
                        public_repos = user.publicRepos,
                        followers = user.followers,
                        following = user.following
                    )
                }
            }.onFailure {
                _userList.value = emptyList()
            }
            _isLoading.value = false
        }
    }

    fun getOnSearch(): LiveData<List<UsersDetails>> = userList
}
