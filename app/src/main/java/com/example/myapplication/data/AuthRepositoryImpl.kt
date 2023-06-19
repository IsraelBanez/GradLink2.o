package com.example.myapplication.data

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(private val auth: FirebaseAuth) : AuthRepository {
    override val currentUser get() = auth.currentUser

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override fun hasUser():Boolean = Firebase.auth.currentUser != null

    override suspend fun authenticateAccount(
        email: String,
        password: String,
        onResult: (Throwable?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { onResult(it.exception) }
    }

    override suspend fun logAccount(
        email: String,
        password: String,
        onResult: (Throwable?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { onResult(it.exception) }
    }

    override suspend fun signOut() { auth.signOut()}

}