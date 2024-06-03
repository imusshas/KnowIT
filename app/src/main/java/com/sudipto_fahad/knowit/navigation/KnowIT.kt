package com.sudipto_fahad.knowit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sudipto_fahad.knowit.MainActivity
import com.sudipto_fahad.knowit.presentation.SignIn
import com.sudipto_fahad.knowit.presentation.VerifyOTP

@Composable
fun KnowIT(viewModel: NavigationViewModel = hiltViewModel()) {

    val navController = rememberNavController()

    val user = viewModel.user.collectAsState().value

    NavHost(
        navController = navController,
        startDestination = if (user.phoneNo.isNotBlank()) {
            Route.PROMPT.name
        } else {
            Route.SIGN_IN.name
        }
    ) {
        composable(route = Route.SIGN_IN.name) {
            SignIn(
                viewModel = viewModel,
              navigateToOTPScreen = {
                  navigate(navController, Route.SIGN_IN.name, Route.VERIFY_OTP.name)
              }
            )
        }


        composable(route = Route.VERIFY_OTP.name) {
            VerifyOTP(
                viewModel = viewModel,
                navigateToPromptScreen = {
                    navigate(navController, Route.VERIFY_OTP.name, Route.PROMPT.name)
                }
            )
        }


        composable(route = Route.PROMPT.name) {
            MainActivity().PromptScreen()
        }
    }

}


private fun navigate(
    navController: NavController,
    source: String,
    destination: String
) {
    navController.navigate(route = destination) {
        popUpTo(route = source) {
            inclusive = true
        }
        launchSingleTop = true
    }

}