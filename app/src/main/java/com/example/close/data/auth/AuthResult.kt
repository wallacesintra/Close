package com.example.close.data.auth

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

//sealed class AuthResult <T>(val userData: T? = null, val message: String? = null) {
//    class Success(userData: T?): AuthResult<T>(userData)
//}

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}