package com.example.navigationlab.ui.favorites

import com.example.navigationlab.data.local.FavoriteStudentEntity

data class FavoritesUiState(
    val favorites: List<FavoriteStudentEntity> = emptyList(),
    val isLoading: Boolean = false
)
