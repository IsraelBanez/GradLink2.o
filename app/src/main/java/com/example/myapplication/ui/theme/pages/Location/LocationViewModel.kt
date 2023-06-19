package com.example.myapplication.ui.theme.pages.Location

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// This would have been used for Live Geo Location

class LocationViewModel : ViewModel(){

    var userLong by mutableStateOf(0.0)
        private set
    var userLad by mutableStateOf(0.0)
        private set
}