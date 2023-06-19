package com.example.myapplication.ui.theme.pages.messages

import androidx.annotation.DrawableRes

// Initial class that stems from the Affirmation Lab, not used at all and was only here as a
// guide for my actual class called "Messenger".
data class Messengers(
    @DrawableRes val imageResourceId: Int,
    val userName: String,

)
