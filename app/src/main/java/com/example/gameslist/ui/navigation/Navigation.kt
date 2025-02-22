package com.example.gameslist.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gameslist.ui.screens.HomeScreen
import com.example.gameslist.ui.viewModel.GamesViewModel
import com.example.gameslist.util.Constants.Screens.HOME_SCREEN

sealed class Screens(val route: String) {
    object Home : Screens(route = HOME_SCREEN)
}

@Composable
fun SetupNavHost(navHostController: NavHostController, gamesViewModel: GamesViewModel) {
    NavHost(navController = navHostController, startDestination = Screens.Home.route) {
        composable(route = Screens.Home.route) {
            HomeScreen(gamesViewModel = gamesViewModel, navController = navHostController)
        }
    }
}