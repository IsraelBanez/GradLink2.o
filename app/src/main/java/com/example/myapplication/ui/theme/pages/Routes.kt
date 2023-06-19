package com.example.myapplication.ui.theme.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.pages.home.HomeScreen
import com.example.myapplication.ui.theme.pages.home.HomeScreenViewModel
import com.example.myapplication.ui.theme.pages.login.LoginScreen
import com.example.myapplication.ui.theme.pages.login.LoginViewModel
import com.example.myapplication.ui.theme.pages.messages.MessageScreen
import com.example.myapplication.ui.theme.pages.messages.MessageScreenTwo
import com.example.myapplication.ui.theme.pages.messages.MessageViewModel
import com.example.myapplication.ui.theme.pages.profile.ProfileScreen
import com.example.myapplication.ui.theme.pages.profile.ProfileViewModel
import com.example.myapplication.ui.theme.pages.signup.SignUpViewModel
import com.example.myapplication.ui.theme.pages.signup.SignupScreen
import com.example.myapplication.ui.theme.pages.settings.SettingScreen
import com.example.myapplication.ui.theme.pages.settings.SettingViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint

enum class Routes {
    Login,
    Signup,
    Home,
    Profile,
    Setting,
    Privacy,

    Message,
    MessageTwo
}

@Composable
fun Routing( loginViewModel: LoginViewModel, signUpViewModel: SignUpViewModel, profileViewModel: ProfileViewModel,
             settingViewModel: SettingViewModel,
             messageViewModel: MessageViewModel,
             homeScreenViewModel: HomeScreenViewModel,
             currentUserLocation: LatLng,
             geoPoint: GeoPoint
){
    val navController = rememberNavController()
    val uiState by loginViewModel.uiState.collectAsState()
    val puiState by profileViewModel.puiState.collectAsState()
    val muiState by messageViewModel.muiState.collectAsState()

    NavHost(navController = navController, startDestination = Routes.Login.name) {

        // Login and Signup Content
        composable(route = Routes.Login.name) {
            LoginScreen(loginViewModel = loginViewModel,
                onNavToHomePage = {navController.navigate(Routes.Home.name) {
                    launchSingleTop = true
                    popUpTo(Routes.Login.name){
                        inclusive = true
                    }
                }},
                onNavToSignUpPage = { navController.navigate(Routes.Signup.name) {
                    launchSingleTop = true
                    popUpTo(Routes.Login.name){
                        inclusive = true
                    }
                }}
            )
        }
        composable(route = Routes.Signup.name) {
            SignupScreen(signupViewModel = signUpViewModel,
                onNavToHomePage = {navController.navigate(Routes.Home.name) {
                    launchSingleTop = true
                    popUpTo(Routes.Signup.name){
                        inclusive = true
                    }
                }}, onNavToLogInPage = {navController.navigate(Routes.Login.name)},
                geoPoint = geoPoint
            )
        }

        // Homepage
        composable(route = Routes.Home.name){
            HomeScreen(homeScreenViewModel = homeScreenViewModel,
                onNavToSettingPage = {navController.navigate(Routes.Setting.name)},
                onNavToPrivacyPage = {navController.navigate(Routes.Privacy.name)},
                onNavToProfilePage = {navController.navigate(Routes.Profile.name)},
                onNavToMessengerPage = {navController.navigate(Routes.Message.name)},
                currentUserLocation = currentUserLocation)
        }

        // Right Menu Bar Content
        composable(route = Routes.Profile.name){
            ProfileScreen(User = uiState,
                onNavToHomePage = { navController.navigate((Routes.Home.name)) },
                profileViewModel = profileViewModel,
                currentUserLocation = currentUserLocation)
        }


        // Left Menu Bar Content
        composable(route = Routes.Setting.name) {
            SettingScreen( settingViewModel = settingViewModel,
            onNavToHomePage = {navController.navigate(Routes.Home.name)},
            onNavToLoginPage = {navController.navigate(Routes.Login.name) {
                launchSingleTop = true
                popUpTo(0){
                    inclusive = true
                }
            }})
        }
        composable(route = Routes.Privacy.name) {
            PrivacyScreen(navController = navController)
        }

        // Messages Content
        composable(route = Routes.Message.name){
            MessageScreen(onNavToHomePage = { navController.navigate(Routes.Home.name) },
                onNavToMessengersTwoPage = {navController.navigate(Routes.MessageTwo.name)},
                loginViewModel = loginViewModel,
                profileViewModel = profileViewModel,
                messageViewModel = messageViewModel)
        }
        composable(route = Routes.MessageTwo.name){
            MessageScreenTwo(onNavToMessengerPage = { navController.navigate(Routes.Message.name) },
                loginViewModel = loginViewModel,
                profileViewModel = profileViewModel,
                messageViewModel = messageViewModel)
        }

    }
}