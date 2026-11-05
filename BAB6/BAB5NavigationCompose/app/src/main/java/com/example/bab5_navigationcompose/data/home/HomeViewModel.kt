package com.example.bab5_navigationcompose.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bab5_navigationcompose.data.repository.StudentRepository

class HomeViewModel(
    private val repository: StudentRepository
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        loadStudents()
    }

    private fun loadStudents() {
        val studentList = repository.getStudents()
        uiState = if (studentList.isNotEmpty()) {
            HomeUiState(students = studentList)
        } else {
            HomeUiState(errorMessage = "Daftar mahasiswa kosong")
        }
    }

    companion object {
        fun factory(repository: StudentRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HomeViewModel(repository) as T
                }
            }
    }
}