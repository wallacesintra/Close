package com.example.close.data.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume


class UserDataSource(
    private val context: Context,
    private val auth: FirebaseAuth
): UserSource{

    override suspend fun createNewAccountWithEmailAndPassword(
        email: String,
        username: String,
        password: String
    ): Resource<FirebaseUser> = withContext(Dispatchers.Main) {
        suspendCancellableCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    Log.d("createAccount: Success", result.user?.email ?: "")
                    continuation.resume(Resource.Success(result.user!!))
                }
                .addOnFailureListener { e ->
                    Log.w("createAccount: Failure", e.message.toString())
                    continuation.resume(Resource.Error(e.message.toString()))
                }
        }
    }

    override suspend fun signInExistingUserWithEmailAndPassword(
        email: String,
        password: String
    ): Resource<FirebaseUser> = withContext(Dispatchers.Main) {
        suspendCancellableCoroutine<Resource<FirebaseUser>> { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("signInWithEmail:success", task.toString())
                        continuation.resume(Resource.Success(task.result!!.user!!))
                    } else {
                        task.exception?.message?.let { Log.w("signInWithEmail:failure", it) }
                        continuation.resume(Resource.Error(task.exception?.message.toString()))
                    }
                }
        }
    }

    override suspend fun signOutExistingUser() {
        auth.signOut()
        Log.d("signOut:success", "sign out successful")
    }

}

