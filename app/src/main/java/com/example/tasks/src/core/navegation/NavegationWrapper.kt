package com.example.tasks.src.core.navegation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tasks.src.features.auth.presentation.view.AuthScreen
import com.example.tasks.src.features.notes.presentation.view.CreateTaskScreen
import com.example.tasks.src.features.notes.presentation.view.HomeScreen
import com.example.tasks.src.features.notes.presentation.view.UpdateTaskScreen
import com.example.tasks.src.features.welcome.WelcomeScreen

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Welcome") {
        composable("Welcome") {
            WelcomeScreen { navController.navigate("Auth") }
        }

        composable("Auth") {
            AuthScreen { userId ->
                navController.navigate("Home/$userId")
            }
        }


        composable(
            route = "Home/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            HomeScreen(
                userId = userId,
                onCreateClick = { navController.navigate("FormCreate/$userId") },
                onUpdateClick = { taskId -> navController.navigate("FormUpdate/$userId/$taskId") }
            )
        }


        composable(
            route = "FormCreate/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            CreateTaskScreen(
                userId = userId,
                onNavigateBack = {
                    navController.navigate("Home/$userId") {
                        popUpTo("Home/$userId") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "FormUpdate/{userId}/{taskId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("taskId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
            UpdateTaskScreen(
                userId = userId,
                taskId = taskId,
                onNavigateBack = {
                    navController.navigate("Home/$userId") {
                        popUpTo("Home/$userId") { inclusive = true }
                    }
                }
            )
        }
    }
}