package com.mamsky.pcsprofile.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mamsky.pcsprofile.screen.detail.ProfileDetailScreen
import com.mamsky.pcsprofile.screen.list.ProfileListScreen

@Composable
fun MyNavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = "profileList"
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(route = startDestination) {
            ProfileListScreen(navController)
        }

        composable(route = "detailScreen/{id}") {
            val id = it.arguments?.getString("id") ?: "Null"
            ProfileDetailScreen(navController, id)
        }
    }
}