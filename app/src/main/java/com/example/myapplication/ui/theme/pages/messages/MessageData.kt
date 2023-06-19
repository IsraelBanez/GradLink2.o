package com.example.myapplication.ui.theme.pages.messages

import com.example.myapplication.R

// Initial class list that stems from the Affirmation Lab, not used at all and was only here as a
// guide for my actual class called "Messenger" and my actual list in the Messaging screens

class MessageData(){
    fun loadMessengers(): List<Messengers> {
        return listOf<Messengers>(
            Messengers(R.drawable.profile, "Bob"),
            Messengers(R.drawable.profile, "Gwen"),
            Messengers(R.drawable.profile, "Jenny"),
            Messengers(R.drawable.profile, "Rocky"),
            Messengers(R.drawable.profile, "Butler"),
            Messengers(R.drawable.profile, "Winter"),
        )
    }
}
