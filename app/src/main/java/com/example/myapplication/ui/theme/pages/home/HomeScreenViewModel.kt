package com.example.myapplication.ui.theme.pages.home

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AuthRepositoryImpl
import com.example.myapplication.data.StorageRepositoryImpl
import com.example.myapplication.ui.theme.pages.login.LoginUIState
import com.example.myapplication.ui.theme.pages.profile.ProfileUIState
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.snapshots
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val authRepositoryImpl: AuthRepositoryImpl,
                          private val storageRepositoryImpl: StorageRepositoryImpl
) : ViewModel()  {

    val hasUser: Boolean
        get() = authRepositoryImpl.hasUser()
    var data: DocumentSnapshot? = null
    var currentMessenger = authRepositoryImpl.currentUserId
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
    fun getLocation(target: String) : GeoPoint? {
        if (data != null){
            return data!!.getGeoPoint(target)
        }
        return null
    }

    fun getList(){

    }
}