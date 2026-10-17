package com.example.assessment_souce_code_faiz.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assessment_souce_code_faiz.data.UsersDetails
import com.example.assessment_souce_code_faiz.domain.model.Repository
import com.example.assessment_souce_code_faiz.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModels @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val list = MutableLiveData<UsersDetails>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _reposList = MutableLiveData<List<Repository>>()
    val reposList: LiveData<List<Repository>> = _reposList

    private val _isLoadingRepos = MutableStateFlow(false)
    val isLoadingRepos: StateFlow<Boolean> = _isLoadingRepos

    fun setDetails(username: String) {
        if (username.isBlank()) return
        viewModelScope.launch {
            _isLoading.value = true
            val result = userRepository.getUserDetail(username)
            result.onSuccess { user ->
                list.value = UsersDetails(
                    login = user.login,
                    avatar_url = user.avatarUrl,
                    name = user.name ?: "",
                    company = user.company ?: "",
                    location = user.location ?: "",
                    bio = user.bio ?: "",
                    public_repos = user.publicRepos,
                    followers = user.followers,
                    following = user.following
                )
            }
            _isLoading.value = false
        }
    }

    fun setRepos(username: String) {
        if (username.isBlank()) return
        viewModelScope.launch {
            _isLoadingRepos.value = true
            val result = userRepository.getUserRepos(username)
            result.onSuccess { repos ->
                _reposList.value = repos
            }.onFailure {
                _reposList.value = emptyList()
            }
            _isLoadingRepos.value = false
        }
    }

    fun getOnDetails(): LiveData<UsersDetails> = list
}
