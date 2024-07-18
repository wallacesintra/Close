package com.example.close.data.location

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.close.data.location.model.FriendLocationDetail
import com.example.close.data.location.model.FriendsLocation
import com.example.close.data.location.model.LocationModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class LocationDataSource(
    private val context: Context,
    private val firestoreDB: FirebaseFirestore,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val currentLocationRequest: com.google.android.gms.location.LocationRequest
): LocationSource {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    private val closeFriendsLocation = "CloseFriendsCoordinates"

    override suspend fun fetchCurrentLocation(): Flow<LocationModel> {
        return callbackFlow {
            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    try {
                        trySend(
                            LocationModel(
                                latitude = result.lastLocation?.latitude ?: 0.0,
                                longitude = result.lastLocation?.longitude ?: 0.0,
                            )
                        )
                    } catch (e: Exception) {
                        Log.e("Location error", e.message.toString())
                    }
                }
            }

            if (ActivityCompat.checkSelfPermission(
                    context, android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context, android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    LOCATION_PERMISSION_REQUEST_CODE
                )

            } else {

                fusedLocationProviderClient.requestLocationUpdates(
                    currentLocationRequest,
                    callback,
                    Looper.getMainLooper()
                )
                    .addOnSuccessListener {
                        Log.d("Location fetch request: Success", "successful")
                    }
                    .addOnFailureListener { e ->
                        e.message?.let { Log.w("Location fetch request: Failure", it) }
                        close(e)
                    }

                awaitClose {
                    fusedLocationProviderClient.removeLocationUpdates(callback)
                }
            }
        }

    }

    override suspend fun getFriendsLocation(userUID: String): Flow<List<FriendLocationDetail>> =
       callbackFlow {
           val listener = firestoreDB.collection(closeFriendsLocation)
               .document(userUID)
               .addSnapshotListener { snapshot, error ->
                   if (error != null){
                       close()
                       Log.w("Location Sharing", "fetching friends location failed with $error")
                       return@addSnapshotListener
                   }

                   val friendsLocation = snapshot?.toObject<FriendsLocation>()
                   Log.d("Location Sharing", "friends location ${friendsLocation!!.friendsLocationList.size}")

                   trySend(friendsLocation.friendsLocationList)

               }

           awaitClose { listener.remove() }
    }

//    override suspend fun getFriendsLocationNotFlow(userUID: String): List<FriendLocationDetail> = withContext(Dispatchers.IO) {
//        suspendCancellableCoroutine { continuation ->
//            firestoreDB.collection(closeFriendsLocation).document(userUID)
//                .get()
//                .addOnSuccessListener { locations ->
//
//                    val container = locations.toObject<FriendsLocation>()
//
//                    if (container != null) {
//                        continuation.resume(container.friendsLocationList)
//                    }
//                }
//                .addOnFailureListener { e ->
//                    Log.w("Location Sharing", "get locations failed " + e.message)
//                    continuation.resume(emptyList())
//                }
//
//        }
//    }

//    override suspend fun getFriendsLocationNotFlow(userUID: String): List<FriendLocationDetail> = withContext(Dispatchers.IO) {
//        suspendCancellableCoroutine { continuation ->
//            firestoreDB.collection(closeFriendsLocation).document(userUID)
//                .get()
//                .addOnSuccessListener { documentSnapshot ->
//                    if (documentSnapshot.exists()) {
//                        val container = documentSnapshot.toObject(FriendsLocation::class.java)
//                        container?.let {
//                            continuation.resume(it.friendsLocationList)
//                        } ?: continuation.resume(emptyList())
//                    } else {
//                        Log.w("Location Sharing", "No document found for userUID: $userUID")
//                        continuation.resume(emptyList())
//                    }
//                }
//                .addOnFailureListener { e ->
//                    Log.w("Location Sharing", "get locations failed: ${e.message}")
//                    continuation.resume(emptyList())
//                }
//        }
//    }

    override suspend fun getFriendsLocationNotFlow(userUID: String): List<FriendLocationDetail> = withContext(Dispatchers.IO) {
        suspendCancellableCoroutine { continuation ->
            if (userUID.isBlank()) {
                Log.w("Location Sharing", "Invalid userUID: $userUID")
                continuation.resume(emptyList())
                return@suspendCancellableCoroutine
            }

            firestoreDB.collection(closeFriendsLocation).document(userUID)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val container = documentSnapshot.toObject(FriendsLocation::class.java)
                        container?.let {
                            continuation.resume(it.friendsLocationList)
                        } ?: continuation.resume(emptyList())
                    } else {
                        Log.w("Location Sharing", "No document found for userUID: $userUID")
                        continuation.resume(emptyList())
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("Location Sharing", "get locations failed: ${e.message}")
                    continuation.resume(emptyList())
                }
        }
    }

    /**
     * document that holds locations of users friends location
     **/

    override suspend fun createLocationContainer(userUID: String) {
        val deferred = CompletableDeferred<Unit>()

        val location = FriendsLocation(
            friendUID = userUID,
        )

        firestoreDB.collection(closeFriendsLocation).document(userUID)
            .set(location)
            .addOnSuccessListener {
                Log.d("Location Sharing", "container created ")
                deferred.complete(Unit)
            }
            .addOnFailureListener { e ->
                Log.d("Location Sharing", "container not created" + e.message)
                deferred.completeExceptionally(e)
            }
    }

    override suspend fun shareLocation(friendUID: String ,friendsLocationDetail: FriendLocationDetail) {
        val deferred = CompletableDeferred<Unit>()

        val location = hashMapOf(
            "friendUID" to friendsLocationDetail.userUID,
            "locationDetail" to friendsLocationDetail.locationDetail,
//            "timeStamp" to FieldValue.serverTimestamp()
        )

        firestoreDB.collection(closeFriendsLocation).document(friendUID)
            .update("friendsLocationList", FieldValue.arrayUnion(location))
            .addOnSuccessListener {
                Log.d("Location Sharing", "location sent successful")
                deferred.complete(Unit)
            }
            .addOnFailureListener { e ->
                Log.d("Location Sharing", "location sharing failed" + e.message)
                deferred.completeExceptionally(e)

            }

        withContext(Dispatchers.IO){
            deferred.await()
        }
    }

    override suspend fun checkIfLocationContainerContainer(userUID: String): Boolean {
        // Ensure 'userUID' is not empty and forms a valid document path
        if (userUID.isBlank()) {
            throw IllegalArgumentException("userUID cannot be blank")
        }
        val documentSnapshot = firestoreDB.collection(closeFriendsLocation).document(userUID).get().await()
        return documentSnapshot.exists()
    }
}