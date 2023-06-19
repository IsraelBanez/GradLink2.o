package com.example.myapplication.data

import android.content.Context
import com.example.myapplication.Messenger
import com.example.myapplication.ui.theme.pages.messages.Message
import com.google.firebase.firestore.DocumentSnapshot
import java.util.Objects

interface StorageRepository {
    suspend fun addMessengerProfile(userId: String, messenger: Messenger)
    suspend fun singleUpdate(userId: String, target: String, value: String)
    suspend  fun updateMessengerProfile(userId: String,
                                                 userUniversity: String,
                                                 userAge: String,
                                                 userSex: String,
                                                 userInterest: String,
                                                 userHomeTown: String,
                                                 userBio: String,
    )
    suspend fun getMessenger(userId: String): DocumentSnapshot?
    suspend fun getMessages(userId: String, targetUserId: String, callback: (List<Message>) -> Unit)
}