package com.example.myapplication.data

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.myapplication.Messenger
import com.example.myapplication.ui.theme.pages.messages.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

const val MAIN_COLLECTION_REF = "users"

class StorageRepositoryImpl(private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth) : StorageRepository {

    val rootCollection: CollectionReference = fireStore.collection(MAIN_COLLECTION_REF)

    override suspend fun addMessengerProfile(userId: String, messenger: Messenger){
        rootCollection.document(userId)
            .set(messenger)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    override suspend fun singleUpdate(userId: String, target: String, value: String){
        rootCollection.document(userId).update(target, value)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating $target", e) }
    }

    override suspend fun updateMessengerProfile(userId: String,
                                       userUniversity: String,
                                       userAge: String,
                                       userSex: String,
                                       userInterest: String,
                                       userHomeTown: String,
                                       userBio: String,
    ){
        rootCollection.document(userId)
            .update(
            mapOf(
                "userUniversity" to userUniversity,
                "userAge" to userAge,
                "userSex" to userSex,
                "userInterest" to userInterest,
                "userHomeTown" to userHomeTown,
                "userBio" to userBio,
            ),
        )
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated Profile!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating Profile", e) }
    }

    override suspend fun getMessenger(userId: String) : DocumentSnapshot{
        var documentSnapshot = rootCollection.document(userId).get().await()

        // not needed but good to have in order to check for errors
        rootCollection.document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    documentSnapshot = document

                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

        // not needed as well
        Log.d(TAG, "DocumentSnapshot data pulled out: $documentSnapshot")

        return documentSnapshot
    }
    override suspend fun getMessages(userId: String, targetUserId: String, callback: (List<Message>) -> Unit) {
        var dataList =  mutableListOf<Message>()
        var data: Message

        rootCollection.document(userId)
            .collection("messengers")
            .document(targetUserId)
            .collection("messages")
            .orderBy("time")
            .get()
            .addOnSuccessListener { snapShot ->
                for (document in snapShot.documents) {
                    data = document.toObject<Message>()!!
                    dataList.add(document.toObject<Message>()!!)
                    Log.d(ContentValues.TAG, "DocumentSnapshot of Messages: $data")
                }
                Log.d(ContentValues.TAG, "DocumentSnapshot datalist of Messages: $dataList")

                callback(dataList)

            }
            .addOnFailureListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot of Message Error")
            }
    }
}