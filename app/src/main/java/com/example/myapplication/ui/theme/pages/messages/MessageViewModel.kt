package com.example.myapplication.ui.theme.pages.messages

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Messenger
import com.example.myapplication.data.AuthRepositoryImpl
import com.example.myapplication.data.StorageRepositoryImpl
import com.example.myapplication.ui.theme.pages.login.LoginUIState
import com.example.myapplication.ui.theme.pages.profile.ProfileUIState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MessageViewModel(private val authRepositoryImpl: AuthRepositoryImpl,
                       private val storageRepositoryImpl: StorageRepositoryImpl
) : ViewModel() {

    private val _message = MutableLiveData("")
    val message: LiveData<String> = _message

    private var _messages = MutableLiveData(emptyList<Map<String, Any>>().toMutableList())
    val messages: LiveData<MutableList<Map<String, Any>>> = _messages

    fun updateMessageValue(message: String) {
        _message.value = message
    }

    private val _muiState = MutableStateFlow(MessageUIState());
    val muiState: StateFlow<MessageUIState> = _muiState.asStateFlow();

    val hasUser: Boolean
        get() = authRepositoryImpl.hasUser()

    var data: DocumentSnapshot? = null
    var targetData: DocumentSnapshot? = null

    val currentMessenger = authRepositoryImpl.currentUserId


    // Message Info
    var userSearch by mutableStateOf("")
        private set

    var userMessage by mutableStateOf("")
        private set

    var userTargetedMessenger by mutableStateOf("")
        private set

    fun updateTargetMessenger(target: String){
        userTargetedMessenger = target
    }
    // Get user input for information
    fun updateUserSearch(search: String){
        userSearch = search
    }

    fun updateMessage(message: String){
        userMessage = message
    }

    fun getDocument() {
        viewModelScope.launch {
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

    fun getTargetDocument(targetID: String) {
        viewModelScope.launch {
            targetData = storageRepositoryImpl.getMessenger(targetID)
        }
    }
    fun getTargetField(target: String) : String? {
        if (targetData != null){
            return targetData!!.getString(target)
        }
        return null
    }

    fun addMessages(targetUserId: String){
        val message: String = _message.value ?: throw IllegalArgumentException("message empty")
        if (message.isNotEmpty()) {
            // Store it for sender
            storageRepositoryImpl.rootCollection.document(Firebase.auth.currentUser!!.uid)
                .collection("messengers")
                .document(targetUserId)
                .collection("messages")
                .document()
                .set(
                hashMapOf(
                    "message" to message,
                    "sender" to Firebase.auth.currentUser!!.uid,
                    "time" to System.currentTimeMillis()
                )
            )
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Message Stored")
                    _message.value = ""
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "Failed to Store", exception)
                }

            // Store it for receiver
            storageRepositoryImpl.rootCollection.document(targetUserId)
                .collection("messengers")
                .document(Firebase.auth.currentUser!!.uid)
                .collection("messages")
                .document()
                .set(
                    hashMapOf(
                        "message" to message,
                        "sender" to Firebase.auth.currentUser!!.uid,
                        "time" to System.currentTimeMillis()
                    )
                )
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Message Sent")
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "Failed to Send", exception)
                }
        }

    }
    fun getMessages(targetUserId: String, callback: (List<Message>) -> Unit){
        viewModelScope.launch{
            storageRepositoryImpl.getMessages(userId = currentMessenger,
            targetUserId = targetUserId, callback = callback)
        }

    }
    fun getMessagesStored() {
        if (authRepositoryImpl.hasUser()) {
            storageRepositoryImpl.rootCollection.document(Firebase.auth.currentUser!!.uid)
                .collection("messengers")
                .document(userTargetedMessenger)
                .collection("messages")
                .orderBy("time")
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        Log.d(ContentValues.TAG, "Listen failed.", e)
                    }

                    val list = emptyList<Map<String, Any>>().toMutableList()

                    if (value != null) {
                        for (doc in value) {
                            val data = doc.data

                            list.add(data)
                        }
                    }
                    Log.d(ContentValues.TAG, "Listen failed 2 $list", e)
                    updateMessages(list)
                }
        }
    }

    private fun updateMessages(list: MutableList<Map<String, Any>>) {
        _messages.value = list.asReversed()
    }
}

// Can have its own file
data class Message(
    val message: String = "",
    val sender: String = "",
    val time: Long = 0
)
