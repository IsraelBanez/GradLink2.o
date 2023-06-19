package com.example.myapplication.ui.theme.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun PrivacyScreen(navController: NavHostController) {
    Box(contentAlignment = Alignment.Center,
    modifier = Modifier.fillMaxSize()){
        Column() {
            Text(text = "Privacy Policy")
            Button(onClick = {navController.navigate(Routes.Home.name)}) {
                Text(text = "Return")
            }
        }

    }
}