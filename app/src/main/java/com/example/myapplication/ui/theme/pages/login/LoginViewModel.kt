package com.example.myapplication.ui.theme.pages.login

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AuthRepositoryImpl
import com.example.myapplication.ui.theme.pages.login.LoginUIState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.regex.Pattern


class LoginViewModel(private val authRepositoryImpl: AuthRepositoryImpl) : ViewModel() {
    //UI State
    private val _uiState = MutableStateFlow(LoginUIState());
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow();

    val hasUser: Boolean
        get() = authRepositoryImpl.hasUser()
    // Login and Signup information
    var userEmail by mutableStateOf("")
        private set
    var userPassword by mutableStateOf("")
        private set

    // Get user input for information
    fun updateUserEmail(email: String) {
        userEmail = email
    }
    fun updateUserPassword(password: String) {
        userPassword = password
    }

    // Check for correct Email format and Password Length
    fun checkValidUser() : Boolean {
        if (Pattern.matches("^(.+)@(.+)\$", userEmail)) {
            _uiState.update { currentUserState ->
                currentUserState.copy(invalidEmail = false)}
            if (userPassword.length >= 8) {
                _uiState.update { currentUserState ->
                    currentUserState.copy(
                        userEmail = userEmail,
                        userPassword = userPassword,
                        invalidPassword = false
                    )
                }
                updateUserEmail("")
                updateUserPassword("")
                return true;

            } else{
                _uiState.update { currentUserState ->
                    currentUserState.copy(
                        invalidPassword = true
                    )
                }
            }
        } else{
            if (userPassword.length >= 8){
                _uiState.update { currentUserState ->
                    currentUserState.copy(
                        invalidEmail = true,
                        invalidPassword = false
                    )
                }
            }else {
                _uiState.update { currentUserState ->
                    currentUserState.copy(
                        invalidPassword = true,
                        invalidEmail = true
                    )
                }
            }
        }
        updateUserEmail("")
        updateUserPassword("")
        return false;
    }
    fun authenticateUser(context: Context) {
        viewModelScope.launch {
            authRepositoryImpl.authenticateAccount(
                _uiState.value.userEmail,
                _uiState.value.userPassword
            )
            { status ->
                if (status == null) {
                    Toast.makeText(context, "Successful Logged In", Toast.LENGTH_SHORT).show()
                    _uiState.update { currentUserState ->
                        currentUserState.copy(
                            isSuccess = true,
                            isError = false
                        )
                    }
                } else {
                    Toast.makeText(context, "Failed to Log In", Toast.LENGTH_SHORT).show()
                    _uiState.update { currentUserState ->
                        currentUserState.copy(
                            isError = true,
                            isSuccess = false
                        )
                    }
                }
            }
        }

    }
}