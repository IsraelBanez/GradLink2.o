package com.example.myapplication.data


import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    val currentUserId: String
    fun hasUser(): Boolean

    suspend fun authenticateAccount(email: String, password: String, onResult: (Throwable?) -> Unit)
    suspend fun logAccount(email: String, password: String, onResult: (Throwable?) -> Unit)
    suspend fun signOut()
}