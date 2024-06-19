package com.example.close.data.database

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.close.presentation.models.CloseUserData
import com.google.firebase.firestore.FirebaseFirestore

/*
* user database
* */
class CloseUserDataSource(
    private val context: Context,
    private val firestoreDb: FirebaseFirestore
): CloseUserSource {
    override suspend fun addNewCloseUser(newUser: CloseUserData) {
        firestoreDb.collection("closeUsers").document(newUser.uid)
            .set(newUser)
            .addOnCompleteListener {task ->
                if (task.isSuccessful){
                    Log.d("firestore:add user", "user added successful:"+ newUser.username)
                } else {
                    Log.w("firestore:add user", task.exception)

                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
//            .addOnSuccessListener {Log.d("Add user", "user added successful:"+ newUser.username)}
//            .addOnFailureListener { e -> Log.w("Add user", "user do not added " + e.message) }
    }
}