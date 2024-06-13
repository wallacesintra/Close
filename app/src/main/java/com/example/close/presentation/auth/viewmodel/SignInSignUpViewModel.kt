package com.example.close.presentation.auth.viewmodel

import androidx.lifecycle.ViewModel

class SignInSignUpViewModel: ViewModel() {

    fun isValidEmail(email: String): Boolean{
        val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()
        return email.matches(emailRegex)
    }

    fun checkConfirmedPasswordMatch(password: String, confirmPassword: String): Boolean{
        return password == confirmPassword
    }

}