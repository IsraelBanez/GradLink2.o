package com.example.myapplication

import com.google.firebase.firestore.GeoPoint
import java.sql.Timestamp

data class Messenger(
    val uid: String  = "",
    val userFirst: String = "",
    val userLast: String = "",
    val userEmail: String = "",
    val password: String ="",
    val userUniversity: String = "",
    val userAge: String = "",
    val userSex: String = "",
    val userInterest: String = "",
    val userHomeTown: String = "",
    val userBio: String = "",
    val location: GeoPoint = GeoPoint(0.0, 0.0)
)