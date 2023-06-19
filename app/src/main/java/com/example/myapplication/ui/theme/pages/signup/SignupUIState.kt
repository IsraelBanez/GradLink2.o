package com.example.myapplication.ui.theme.pages.signup

data class SignupUIState(
    val userFirst: String = "",
    val userLast: String = "",
    val userEmail: String = "",
    val userPassword: String = "",

    val emptyFirst: Boolean = false,
    val emptyLast: Boolean = false,
    val invalidPassword: Boolean = false,
    val invalidEmail: Boolean = false,

    val isSuccess: Boolean = false,
    val isError: Boolean = false,

)