package com.example.close.data.database

import android.util.Log
import com.example.close.presentation.models.CloseUserData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.CompletableDeferred

class CloseUserDataSource(
    private val firestoreDb: FirebaseFirestore
): CloseUserSource {
    override suspend fun addNewCloseUser(newUser: CloseUserData) {
        val deferred = CompletableDeferred<Unit>()

        firestoreDb.collection("closeUsers").document(newUser.uid)
            .set(newUser)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("firestore:add user", "user added successful: ${newUser.username}")
                } else {
                    Log.w("firestore:add user", task.exception.toString())
                }
                deferred.complete(Unit)
            }

        withContext(Dispatchers.IO) {
            deferred.await()
        }
    }

    override suspend fun getSignInUser(uid: String): CloseUserData {
        val deferred = CompletableDeferred<CloseUserData>()

        firestoreDb.collection("closeUsers").document(uid)
            .get()
            .addOnSuccessListener { user ->
                val userData = user.toObject<CloseUserData>()
                Log.d("get user: successful", "user uid: " + userData?.email)

                if (userData != null){
                    deferred.complete(userData)
                }

            }
            .addOnFailureListener {e ->
                Log.w("get user: failure", e.message.toString())
                deferred.completeExceptionally(e)
            }

        return withContext(Dispatchers.IO){
            deferred.await()
        }
    }

}