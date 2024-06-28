package com.example.close.data.cometChat

import android.util.Log
import com.cometchat.chat.core.CometChat
import com.cometchat.chat.exceptions.CometChatException
import com.cometchat.chat.models.User
import com.example.close.data.cometChat.models.CometAuthDto
import com.google.gson.Gson
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class CometChatAuthImp(
    private val appID: String,
    private val region: String,
    private val restApiKey: String
): CometChatAuth {

    private val cometKey = "fecdb9d449e1afbc6b9c9a2e332a7705f6940734"

    override suspend fun getAuthToken(userId: String): CometAuthDto {
        val deferred = CompletableDeferred<CometAuthDto>()
        val client = OkHttpClient()

        val mediaType = "application/json".toMediaTypeOrNull()
        val body = RequestBody.create(mediaType, "{\"force\":true}")
        val request = Request.Builder()
            .url("https://${appID}.api-${region}.cometchat.io/v3/users/${userId}/auth_tokens")
            .post(body)
            .addHeader("accept", "application/json")
            .addHeader("content-type", "application/json")
            .addHeader("apikey", restApiKey)
            .build()

        val response = client.newCall(request).execute()
        deferred.complete(Gson().fromJson(response.body?.string(), CometAuthDto::class.java))

        return withContext(Dispatchers.IO){
            deferred.await()
        }
    }

    override suspend fun createCometUser(userId: String, username: String) {
        val user = User()
        user.uid = userId
        user.name = username

        CometChat.createUser(user,cometKey, object : CometChat.CallbackListener<User>() {
            override fun onSuccess(user: User) {
                Log.d("CometChat:createUser", user.toString())
            }

            override fun onError(e: CometChatException) {
                Log.e("CometChat:createUser", e.message!!)
            }
        })
    }

    override suspend fun logInUser(userId: String) {
        withContext(Dispatchers.IO){
            Log.d("CometChat LogIn: loading", "LogIn Loading....")

            if(CometChat.getLoggedInUser() == null){

                val authToken = getAuthToken(userId = userId).data.authToken
                CometChat.login(
                    authToken,
                    object: CometChat.CallbackListener<User>(){
                        override fun onSuccess(p0: User?) {
                            Log.d("CometChat LogIn: Successful", "Login successful")
                        }

                        override fun onError(p0: CometChatException?) {
                            Log.e("CometChat LogIn: Failure", "Login failed with exception : " + p0?.message)
                        }
                    }
                )
            }else {
                Log.d("CometChat LogIn: Already", "user logged already....")
            }
        }
    }

    override suspend fun logOutUser() {
        withContext(Dispatchers.IO){
            CometChat.logout(object : CometChat.CallbackListener<String>() {
                override fun onSuccess(p0: String?) {
                    Log.d("CometChat: LogOut: Successful", "Logout completed successfully")
                }

                override fun onError(p0: CometChatException?) {
                    Log.d("CometChat: LogOut: Failure", "Logout failed with exception: " + p0?.message)
                }
            })
        }
    }


}