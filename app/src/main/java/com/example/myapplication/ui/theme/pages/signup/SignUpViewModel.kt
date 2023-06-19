package com.example.myapplication.ui.theme.pages.signup

import android.content.Context
import android.location.Location
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Messenger
import com.example.myapplication.data.AuthRepositoryImpl
import com.example.myapplication.data.StorageRepositoryImpl
import com.example.myapplication.ui.theme.pages.login.LoginUIState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SignUpViewModel(private val authRepositoryImpl: AuthRepositoryImpl,
    private val storageRepositoryImpl: StorageRepositoryImpl, ) : ViewModel()  {
    // Connect to Firebase Auth
    val hasUser: Boolean
        get() = authRepositoryImpl.hasUser()
    //UI State
    private val _uiState = MutableStateFlow(SignupUIState());
    val uiState: StateFlow<SignupUIState> = _uiState.asStateFlow();

    // Initiate Signup information
    var userFirst by mutableStateOf("")
        private set
    var userLast by mutableStateOf("")
        private set
    var userEmail by mutableStateOf("")
        private set
    var userPassword by mutableStateOf("")
        private set

    // Store user input for information
    fun updateUserFirst(first: String){
        userFirst = first
    }
    fun updateUserLast(last: String){
        userLast = last
    }
    fun updateUserEmail(email: String) {
        userEmail = email
    }
    fun updateUserPassword(password: String) {
        userPassword = password
    }
    // make better so it does not delete all information

    // Make sure First and Last name is not empty
    fun checkValidName() : Boolean{
        if (userFirst.isNotEmpty()){
            _uiState.update{ currentUserSate ->
                currentUserSate.copy(emptyFirst = false)}
            if (userLast.isNotEmpty()){
                _uiState.update{ currentUserSate ->
                    currentUserSate.copy(emptyLast = false,
                        userFirst = userFirst,
                        userLast = userLast)}
                updateUserFirst("")
                updateUserLast("")
                return true
            } else {
                _uiState.update{ currentUserSate ->
                    currentUserSate.copy(emptyFirst = false, emptyLast = true)}
            }
        } else{
            if (userFirst.isEmpty() && userLast.isNotEmpty()){
                _uiState.update{ currentUserSate ->
                    currentUserSate.copy(emptyFirst = true, emptyLast = false)}
            } else if (userFirst.isNotEmpty() && userLast.isEmpty()) {
                _uiState.update{ currentUserSate ->
                    currentUserSate.copy(emptyFirst = false, emptyLast = true)}
            } else {
                _uiState.update{ currentUserSate ->
                    currentUserSate.copy(emptyFirst = true, emptyLast = true)}
            }
        }
        updateUserFirst("")
        updateUserLast("")
        return false
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

    fun logUser(context: Context) {
        viewModelScope.launch {
            authRepositoryImpl.logAccount(
                _uiState.value.userEmail,
                _uiState.value.userPassword
            )
            { status ->
                if (status == null) {
                    Toast.makeText(context, "Successful Sign Up", Toast.LENGTH_SHORT).show()
                    _uiState.update { currentUserState ->
                        currentUserState.copy(
                            isSuccess = true,
                            isError = false
                        )
                    }
                } else {
                    Toast.makeText(context, "Failed to Sign Up $status", Toast.LENGTH_SHORT).show()
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

    fun addProfile(geoPoint: GeoPoint){
        viewModelScope.launch  {

            storageRepositoryImpl.addMessengerProfile(userId = authRepositoryImpl.currentUserId
                , Messenger(
                    uid = authRepositoryImpl.currentUserId,
                    userFirst = _uiState.value.userFirst,
                userLast = _uiState.value.userLast,
                userEmail = _uiState.value.userEmail,
                password = _uiState.value.userPassword,
                userUniversity = "",
                userAge = "",
                userSex = "",
                userInterest = "",
                userHomeTown = "",
                userBio = "",
                    location = geoPoint
            ))
        }
    }



}