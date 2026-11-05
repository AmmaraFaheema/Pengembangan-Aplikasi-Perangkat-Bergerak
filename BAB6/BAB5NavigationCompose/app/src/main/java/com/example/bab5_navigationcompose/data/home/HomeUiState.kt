package com.example.bab5_navigationcompose.ui.home

import com.example.bab5_navigationcompose.data.model.Student

data class HomeUiState(
    val students: List<Student> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)