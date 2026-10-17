package com.example.assessment_souce_code_faiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.assessment_souce_code_faiz.ui.screen.DetailScreen
import com.example.assessment_souce_code_faiz.ui.screen.MainScreen
import com.example.assessment_souce_code_faiz.ui.screen.SplashScreen
import com.example.assessment_souce_code_faiz.ui.theme.AppTheme
import com.example.assessment_souce_code_faiz.viewmodel.DetailsViewModels
import com.example.assessment_souce_code_faiz.viewmodel.FollowsViewModel
import com.example.assessment_souce_code_faiz.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "splash"
                ) {
                    composable("splash") {
                        SplashScreen(
                            onTimeout = {
                                navController.navigate("main") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("main") {
                        val mainViewModel: MainViewModel = hiltViewModel()
                        MainScreen(
                            viewModel = mainViewModel,
                            onUserClick = { username ->
                                navController.navigate("detail/$username")
                            }
                        )
                    }

                    composable(
                        route = "detail/{username}",
                        arguments = listOf(navArgument("username") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val username = backStackEntry.arguments?.getString("username").orEmpty()
                        val detailsViewModel: DetailsViewModels = hiltViewModel()
                        val followsViewModel: FollowsViewModel = hiltViewModel()
                        DetailScreen(
                            username = username,
                            detailsViewModel = detailsViewModel,
                            followsViewModel = followsViewModel,
                            onBackClick = { navController.popBackStack() },
                            onUserClick = { selectedUser ->
                                navController.navigate("detail/$selectedUser")
                            }
                        )
                    }
                }
            }
        }
    }
}
