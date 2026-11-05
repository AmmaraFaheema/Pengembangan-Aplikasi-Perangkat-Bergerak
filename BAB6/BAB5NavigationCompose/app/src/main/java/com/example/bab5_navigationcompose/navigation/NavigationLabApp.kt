package com.example.bab5_navigationcompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bab5_navigationcompose.Routes
import com.example.bab5_navigationcompose.data.repository.DefaultStudentRepository
import com.example.bab5_navigationcompose.data.source.StudentLocalDataSource
import com.example.bab5_navigationcompose.ui.screen.DetailScreen
import com.example.bab5_navigationcompose.ui.detail.DetailViewModel
import com.example.bab5_navigationcompose.ui.screen.HomeScreen
import com.example.bab5_navigationcompose.ui.home.HomeViewModel
import com.example.bab5_navigationcompose.ui.screen.AboutScreen
import com.example.bab5_navigationcompose.ui.screen.ProfileScreen

@Composable
fun NavigationLabApp() {
    val dataSource = remember { StudentLocalDataSource() }
    val repository = remember { DefaultStudentRepository(dataSource) }
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {

        composable(Routes.HOME) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.factory(repository)
            )

            HomeScreen(
                uiState = homeViewModel.uiState,
                onStudentClick = { studentId ->
                    navController.navigate(Routes.detail(9999))
                },
                onOpenProfile = {
                    navController.navigate(Routes.PROFILE)
                },
                onOpenAbout = {
                    navController.navigate(Routes.ABOUT)
                }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument("studentId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: 0
            val detailViewModel: DetailViewModel = viewModel(
                factory = DetailViewModel.factory(repository)
            )

            LaunchedEffect(studentId) {
                detailViewModel.loadStudent(studentId)
            }

            DetailScreen(
                uiState = detailViewModel.uiState,
                onOpenProfile = {
                    navController.navigate(Routes.PROFILE)
                },
                onBack = {
                    navController.popBackStack()
                },
                onBackToHome = {
                    navController.popBackStack(Routes.HOME, inclusive = false)
                }
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.ABOUT) {
            AboutScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}