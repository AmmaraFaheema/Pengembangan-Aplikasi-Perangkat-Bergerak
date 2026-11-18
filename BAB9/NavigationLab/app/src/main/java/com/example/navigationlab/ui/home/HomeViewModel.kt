package com.example.navigationlab.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.navigationlab.data.model.Student
import com.example.navigationlab.data.preferences.UserPreferencesRepository
import com.example.navigationlab.data.repository.StudentRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class HomeViewModel(
    private val repository: StudentRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        viewModelScope.launch {
            val savedQuery = preferencesRepository.lastSearchQuery.first()
            val savedSort = preferencesRepository.sortAscending.first()
            _searchQuery.value = savedQuery
            _uiState.value = _uiState.value.copy(isSortAscending = savedSort)
            performSearch(savedQuery)
        }

        observeSearch()
        observeQueryPersistence()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    private fun observeSearch() {
        viewModelScope.launch {
            searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->
                    performSearch(query)
                }
        }
    }

    private fun observeQueryPersistence() {
        viewModelScope.launch {
            searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->
                    preferencesRepository.saveLastSearchQuery(query)
                }
        }
    }

    fun toggleSort() {
        val newSort = !_uiState.value.isSortAscending
        _uiState.value = _uiState.value.copy(
            isSortAscending = newSort,
            students = sortStudents(_uiState.value.students, newSort)
        )
        viewModelScope.launch {
            preferencesRepository.saveSortAscending(newSort)
        }
    }

    private fun sortStudents(list: List<Student>, ascending: Boolean): List<Student> {
        return if (ascending) {
            list.sortedBy { it.name }
        } else {
            list.sortedByDescending { it.name }
        }
    }

    fun searchStudents() {
        val query = searchQuery.value
        viewModelScope.launch {
            performSearch(query)
        }
    }

    private suspend fun performSearch(query: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )
        try {
            val rawData = if (query.isBlank()) {
                repository.getStudents()
            } else {
                repository.searchStudents(query)
            }
            val sortedData = sortStudents(rawData, _uiState.value.isSortAscending)
            _uiState.value = _uiState.value.copy(
                students = sortedData,
                isLoading = false,
                errorMessage = null
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                errorMessage = e.message ?: "Pencarian gagal",
                isLoading = false
            )
        }
    }

    fun retry() {
        val query = searchQuery.value
        viewModelScope.launch {
            performSearch(query)
        }
    }

    fun refresh() {
        val query = searchQuery.value
        viewModelScope.launch {
            performSearch(query)
        }
    }

    companion object {
        fun factory(
            repository: StudentRepository,
            preferencesRepository: UserPreferencesRepository
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HomeViewModel(repository, preferencesRepository) as T
                }
            }
    }
}
