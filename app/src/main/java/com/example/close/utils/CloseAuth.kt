package com.example.close.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class CloseAuth(
    private val context: Context,
    private val auth: FirebaseAuth
) {
    suspend fun createAccount(email: String, password: String): FirebaseUser? {

        var user: FirebaseUser? = null
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //Sign in success
                    Log.d("createUserWithEmail:success", task.toString())
                    user = auth.currentUser
                } else {
                    // Sign in fails
                    Log.w("createUserWithEmail:failure", task.exception)

                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }

        return user
    }

    suspend fun signIn(email: String, password: String){

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //sign in successful
                    Log.d("signInWithEmail:success", task.toString())
                    val user = auth.currentUser
                } else {
                    Log.w("signInWithEmail:failure", task.exception)
                }
            }
    }


}