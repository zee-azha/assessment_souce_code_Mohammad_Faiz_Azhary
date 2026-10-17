package com.example.assessment_souce_code_faiz.data

data class RepoItem(
    val id: Long = 0,
    val name: String = "",
    val description: String? = null,
    val stargazers_count: Int = 0,
    val forks_count: Int = 0,
    val language: String? = null,
    val html_url: String = ""
)
