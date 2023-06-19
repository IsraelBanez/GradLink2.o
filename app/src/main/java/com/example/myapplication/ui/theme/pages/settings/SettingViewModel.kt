package com.example.myapplication.ui.theme.pages.settings

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AuthRepositoryImpl
import com.example.myapplication.ui.theme.pages.signup.SignupUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingViewModel( private val authRepositoryImpl: AuthRepositoryImpl) : ViewModel() {
    val hasUser: Boolean
        get() = authRepositoryImpl.hasUser()
    private val _uiState = MutableStateFlow(SettingUIState());
    val uiState: StateFlow<SettingUIState> = _uiState.asStateFlow();

    fun logUserOut(context: Context) {
        viewModelScope.launch{
            try {
                authRepositoryImpl.signOut()
                Toast.makeText(context, "User has logged out", Toast.LENGTH_SHORT).show()
                _uiState.update { currentUserState ->
                    currentUserState.copy(
                        loggedOut = true
                    )
                }

            } catch(e: Exception){
                Toast.makeText(context, "Failed to log out", Toast.LENGTH_SHORT).show()
                _uiState.update { currentUserState ->
                    currentUserState.copy(
                        loggedOut = false
                    )
                }
            }
        }
    }
    fun redo(){
        _uiState.update { currentUserState ->
            currentUserState.copy(
                loggedOut = false)
        }
    }
}