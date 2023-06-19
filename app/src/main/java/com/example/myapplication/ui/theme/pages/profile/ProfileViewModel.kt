package com.example.myapplication.ui.theme.pages.profile

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AuthRepositoryImpl
import com.example.myapplication.data.StorageRepositoryImpl
import com.example.myapplication.ui.theme.pages.login.LoginUIState
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(private val authRepositoryImpl: AuthRepositoryImpl,
                       private val storageRepositoryImpl: StorageRepositoryImpl
) : ViewModel()  {
    private val _puiState = MutableStateFlow(ProfileUIState());
    val puiState: StateFlow<ProfileUIState> = _puiState.asStateFlow();

    private val _luiState = MutableStateFlow(LoginUIState());
    val luiState: StateFlow<LoginUIState> = _luiState.asStateFlow();

    val hasUser: Boolean
        get() = authRepositoryImpl.hasUser()
    var data: DocumentSnapshot? = null

    // Profile information
    var userName by mutableStateOf("")
        private set
    var userUniversity by mutableStateOf("")
        private set
    var userAge by mutableStateOf("")
        private set
    var userSex by mutableStateOf("")
        private set
    var userHomeTown by mutableStateOf("")
        private set
    var userInterest by mutableStateOf("")
        private set
    var userBio by mutableStateOf("")
        private set
    var user by mutableStateOf("")
        private set
    // Get user input for information
    fun updateUserName(name: String){
        userName = name
        _puiState.update { currentUserState ->
            currentUserState.copy(
                userFullName = name
            )
        }
    }
    fun updateUserUniversity(university: String){
        userUniversity = university
        _puiState.update { currentUserState ->
            currentUserState.copy(
                userUniversity = university
            )
        }

    }
    fun updateUserAge(age: String){
        userAge = age
        _puiState.update { currentUserState ->
            currentUserState.copy(
                userAge = age
            )
        }
    }
    fun updateUserSex(sex: String){
        userSex = sex
        _puiState.update { currentUserState ->
            currentUserState.copy(
                userSex = sex
            )
        }
    }
    fun updateUserHomeTown(hometown: String){
        userHomeTown = hometown
        _puiState.update { currentUserState ->
            currentUserState.copy(
                userHomeTown = hometown
            )
        }
    }
    fun updateUserInterest(interest: String){
        userInterest = interest
        _puiState.update { currentUserState ->
            currentUserState.copy(
                userInterest = interest
            )
        }
    }
    fun updateUserBio(bio: String){
        userBio = bio
        _puiState.update { currentUserState ->
            currentUserState.copy(
                userBio = bio
            )
        }
    }

    fun resetProfile() {
        _puiState.value = ProfileUIState(userFullName = "",
        userUniversity = "",
        userAge = "",
        userSex = "",
        userHomeTown = "",
        userInterest = "",
        userBio = "")
        userUniversity = ""
        userName = ""
    }

    fun getDocument(){
        viewModelScope.launch{
            data = storageRepositoryImpl.getMessenger(authRepositoryImpl.currentUserId)
        }
    }

    fun updateSingle (target: String, value: String){
        viewModelScope.launch{
            storageRepositoryImpl.singleUpdate(authRepositoryImpl.currentUserId, target, value)
        }
    }
    fun getField(target: String) : String? {
        if (data != null){
            return data!!.getString(target)
        }
        return null
    }

    fun getLocation(target: String) : GeoPoint? {
        if (data != null){
            return data!!.getGeoPoint(target)
        }
        return null
    }
}