package com.example.myapplication.ui.theme.pages.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.myapplication.ui.theme.pages.Routes
import kotlinx.coroutines.flow.update

@Composable
fun SettingScreen(settingViewModel: SettingViewModel,
                  onNavToHomePage: () -> Unit,
                  onNavToLoginPage: () -> Unit) {
    val settingUiState by settingViewModel.uiState.collectAsState()
    val context = LocalContext.current


    Box(contentAlignment = Alignment.Center,
    modifier = Modifier.fillMaxSize()){
        Column() {
            Button(onClick = {onNavToHomePage.invoke()}
            ){
                Text(text = "Return")
            }
            Button(onClick = {settingViewModel.logUserOut(context)
                onNavToLoginPage.invoke()}

            ) {
                Text(text = "Logout")
            }

            }


    }
}