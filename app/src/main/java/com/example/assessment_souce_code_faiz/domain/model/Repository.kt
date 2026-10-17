package com.example.assessment_souce_code_faiz.domain.model

data class Repository(
    val id: Long,
    val name: String,
    val description: String?,
    val starCount: Int,
    val forkCount: Int,
    val language: String?,
    val htmlUrl: String
)
