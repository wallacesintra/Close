package com.example.close.data.messaging

import android.util.Log
import com.example.close.data.messaging.models.CloseChatRoom
import com.example.close.data.messaging.models.CloseMessage
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CloseMessagingDataSource(
    private val firestoreDb: FirebaseFirestore
): CloseMessaging {

    private val closeChatsCollection = "CloseChats"


    override suspend fun createChatRoom(closeChatRoom: CloseChatRoom) {
        val deferred = CompletableDeferred<Unit>()

        val newChatRoom = hashMapOf(
            "chatUid" to closeChatRoom.chatUid,
            "members" to closeChatRoom.members,
            "messages" to closeChatRoom.messages,
            "timeStamp" to FieldValue.serverTimestamp(),
        )

        firestoreDb.collection(closeChatsCollection).document(closeChatRoom.chatUid)
            .set(newChatRoom)
            .addOnFailureListener { e ->
                Log.w("CloseChat: create room", "creation failed" + e.message)
                deferred.completeExceptionally(e)
            }
            .addOnSuccessListener {
                Log.d("CloseChat: create room", "creation successful")
                deferred.complete(Unit)
            }

        withContext(Dispatchers.IO) {
            deferred.await()
        }
    }

    override suspend fun sendMessage(roomUid: String, senderUid: String, textMessage: String) {
        val deferred = CompletableDeferred<Unit>()

        val newMessage = hashMapOf(
            "messageUid" to UUID.randomUUID().toString(),
            "senderUid" to senderUid,
            "message" to textMessage,
//            "timeStamp" to FieldValue.serverTimestamp(),
        )

        firestoreDb.collection(closeChatsCollection).document(roomUid)
            .update("messages", FieldValue.arrayUnion(newMessage))
            .addOnSuccessListener {
                Log.d("CloseChat: send message", "message sent")
                deferred.complete(Unit)
            }
            .addOnFailureListener { e ->
                Log.w("CloseChat: send message", "message not sent" + e.message)
                deferred.completeExceptionally(e)
            }

        withContext(Dispatchers.IO) {
            deferred.await()
        }
    }


    override suspend fun getChatRoomsForUid(userUid: String): List<CloseChatRoom> =
        withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { continuation ->
                var hasResumed = false // Track if the continuation has already been resumed

                val listenerRegistration = firestoreDb.collection(closeChatsCollection)
                    .whereArrayContains("members", userUid)
                    .addSnapshotListener { value, error ->
                        if (error != null) {
                            if (!hasResumed) {
                                continuation.resumeWithException(error)
                                hasResumed = true
                            }
                            return@addSnapshotListener
                        }

                        if (!hasResumed) {
                            val chatRoomList =
                                value?.documents?.mapNotNull { it.toObject<CloseChatRoom>() }
                                    ?: emptyList()
                            continuation.resume(chatRoomList)
                            hasResumed = true
                        }
                    }

                // Optionally, if you want to remove the listener after resuming to prevent memory leaks
                continuation.invokeOnCancellation {
                    listenerRegistration.remove()
                }
            }
        }

//    override suspend fun getChatRoomMessages(chatRoomUID: String): List<CloseMessage> = withContext(Dispatchers.IO) {
//        suspendCancellableCoroutine { continuation ->
//            var hasResumed = false
//
//            val listenerRegistration = firestoreDb.collection(closeChatsCollection).document(chatRoomUID)
//                .addSnapshotListener { value, error ->
//                    if (error != null) {
//                        if (!hasResumed) {
//                            continuation.resumeWithException(error)
//                            hasResumed = true
//                        }
//                        return@addSnapshotListener
//                    }
//
//                    val chatRoom = value?.toObject<CloseChatRoom>()
//
//                    if(chatRoom != null){
//                        continuation.resume(chatRoom.messages)
//                    }else{
////                      continuation.resumeWithException(KotlinNullPointerException("ChatRoom is null"))
//                        continuation.resume(emptyList())
//                    }
//                }
//
//            continuation.invokeOnCancellation {
//                listenerRegistration.remove()
//            }
//        }
//    }

//    override suspend fun getChatRoomMessages(chatRoomUid: String): Flow<List<CloseMessage>> = flow {
//        val hasResumed = AtomicBoolean(false)
//        val registration = firestoreDb.collection(closeChatsCollection).document(chatRoomUid)
//            .addSnapshotListener { snapshot, error ->
//                if (error != null) {
//                    if (hasResumed.compareAndSet(false, true)) {
//                        throw error
//                    }
//                } else {
//                    snapshot?.let {
//                        val messages = it.toObject<CloseChatRoom>() // Assuming CloseMessage can be directly converted
//                        if (messages != null && hasResumed.compareAndSet(false, true)) {
//                            emit(messages.messages)
////                            registration.remove() // Stop listening to further updates
//                        }
//                    }
//                }
//            }
//        awaitClose { registration.remove() } // Ensure listener is removed if flow collection is stopped
//    }.flowOn(Dispatchers.IO) // Run in IO dispatcher for database operations


    //this
    override suspend fun getChatRoomMessages(chatRoomUid: String): Flow<List<CloseMessage>> =

        callbackFlow {
            val hasResumed = AtomicBoolean(false)

            val registration = firestoreDb.collection(closeChatsCollection).document(chatRoomUid)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        if (hasResumed.compareAndSet(false, true)) {
                            close(error) // Close the flow with an error
                        }
                    } else {
                        snapshot?.let {
                            val messages = it.toObject<CloseChatRoom>()?.messages
                            if (messages != null && hasResumed.compareAndSet(false, true)) {
                                trySend(messages).isSuccess // Attempt to send the messages to the flow
                            }
                        }
                    }
                }

            awaitClose {
                registration.remove()
            }
        }

//    override suspend fun getChatRoomByChatRoomUid(chatroomUid: String): CloseChatRoom  = withContext(Dispatchers.IO){
//        suspendCancellableCoroutine { continuation ->
//            firestoreDb.collection(closeChatsCollection).document(chatroomUid)
//                .get()
//                .addOnSuccessListener { room ->
//                    val chatRoom = room.toObject<CloseChatRoom>()
//
//                    continuation.resume(
//                        CloseChatRoom(
//                            chatUid = chatRoom!!.chatUid,
//                            members = chatRoom.members,
//                            messages = chatRoom.messages,
//                        )
//                    )
//                }
//                .addOnFailureListener { e ->
//                    Log.w("CloseChat: Get Room details", "details fetched successful")
//                    continuation.resumeWithException(e)
//                }
//        }
//    }

//    override suspend fun getChatRoomByChatRoomUid(chatroomUid: String): Flow<CloseChatRoom> = withContext(Dispatchers.IO) {
//        flow {
//            val registration = firestoreDb.collection(closeChatsCollection).document(chatroomUid)
//                .addSnapshotListener { value, error ->
//                    if (error != null) {
//                        throw error
//                    } else {
//                        val chatRoom = value?.toObject<CloseChatRoom>()
//                        if (chatRoom != null) {
//                            emit(chatRoom)
//                        } else {
//                            throw KotlinNullPointerException("ChatRoom is null")
//                        }
//                    }
//                }
//
//            awaitClose {
//                registration.remove()
//            }
//        }
//    }


//    override suspend fun getChatRoomByChatRoomUid(chatroomUid: String): CloseChatRoom = withContext(Dispatchers.IO) {
//        suspendCancellableCoroutine { continuation ->
//            var hasResumed = false // Flag to track if the continuation has been resumed
//
//            // Registering the snapshot listener
//            val registration = firestoreDb.collection(closeChatsCollection).document(chatroomUid)
//                .addSnapshotListener { value, error ->
//                    if (!hasResumed) { // Check if the continuation has not been resumed yet
//                        if (error != null) {
//                            continuation.resumeWithException(error)
//                        } else {
//                            val chatRoom = value?.toObject<CloseChatRoom>()
//                            if (chatRoom != null) {
//                                continuation.resume(chatRoom)
//                            } else {
//                                continuation.resumeWithException(KotlinNullPointerException("ChatRoom is null"))
//                            }
//                        }
//                        hasResumed = true // Mark the continuation as resumed
////                        registration.remove() // Remove the listener to prevent further updates
//                    }
//                }
//
//            // Optionally, remove the listener when the coroutine is cancelled
//            continuation.invokeOnCancellation {
//                registration.remove()
//            }
//        }
//    }

//    override suspend fun getChatRoomsForUid(userUid: String): List<CloseChatRoom> = withContext(Dispatchers.IO){
//        suspendCancellableCoroutine { continuation ->
//            firestoreDb.collection(closeChatsCollection)
//                .whereArrayContains("members", userUid)
//                .addSnapshotListener { value, error ->
//                    if (error != null){
//                        if (continuation.isActive){
//                            continuation.resumeWithException(error)
//                        }
//                        Log.w("CloseChat: message listener", "listen failed, $error")
//                        continuation.resume(emptyList())
//                    }
//
//                    val chatRoomList = mutableListOf<CloseChatRoom>()
//
//                    for (doc in value!!){
//                        val chatRoom = doc.toObject<CloseChatRoom>()
//
//                        chatRoomList.add(
//                            CloseChatRoom(
//                                chatUid = chatRoom.chatUid,
//                                members = chatRoom.members,
//                                messages = chatRoom.messages
//                            )
//                        )
//                    }
//                    Log.d("CloseChat: message listener", " listen successful")
//                    continuation.resume(chatRoomList)
//                }
//        }
//    }



}