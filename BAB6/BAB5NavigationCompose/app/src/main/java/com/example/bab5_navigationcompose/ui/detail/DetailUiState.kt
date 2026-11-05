package com.example.bab5_navigationcompose.ui.detail

import com.example.bab5_navigationcompose.data.model.Student

data class DetailUiState(
    val student: Student? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)