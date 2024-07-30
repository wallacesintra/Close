package com.example.close.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class CryptoManager {

    private val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
    private val keyAlias = "MyKeyAlias"
    private val IV_LENGTH = 12 // 12 bytes for GCM

    init {
        if (!keyStore.containsAlias(keyAlias)) {
            generateKey()
        }
    }

    private fun generateKey() {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    private fun getSecretKey(): SecretKey {
        return (keyStore.getEntry(keyAlias, null) as KeyStore.SecretKeyEntry).secretKey
    }

    fun encryptDouble(data: Double): Pair<String, String> {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        val iv = cipher.iv.copyOf(IV_LENGTH) // Ensure IV is 12 bytes
        val encryptedData = cipher.doFinal(data.toString().toByteArray(Charsets.UTF_8))
        return Pair(encryptedData.toBase64(), iv.toBase64())
    }

    fun decryptDouble(encryptedData: String, iv: String): Double {
        return try {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val spec = GCMParameterSpec(128, iv.fromBase64().copyOf(IV_LENGTH)) // Ensure IV is 12 bytes
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)
            val decryptedData = cipher.doFinal(encryptedData.fromBase64())
            String(decryptedData, Charsets.UTF_8).toDouble()
        } catch (e: Exception) {
            Log.e("CryptoManager", "Decryption error: ${e.message}")
            throw e
        }
    }

    private fun ByteArray.toBase64(): String = android.util.Base64.encodeToString(this, android.util.Base64.DEFAULT)
    private fun String.fromBase64(): ByteArray = android.util.Base64.decode(this, android.util.Base64.DEFAULT)
}

//package com.example.close.utils
//
//import android.security.keystore.KeyGenParameterSpec
//import android.security.keystore.KeyProperties
//import android.util.Base64
//import java.nio.ByteBuffer
//import java.security.KeyStore
//import javax.crypto.Cipher
//import javax.crypto.KeyGenerator
//import javax.crypto.SecretKey
//import javax.crypto.spec.IvParameterSpec
//
//class CryptoManager {
//
//    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
//        load(null)
//    }
//
//    private val encryptCipher get() = Cipher.getInstance(TRANSFORMATION).apply {
//        init(Cipher.ENCRYPT_MODE, getKey())
//    }
//
//    private val decryptCipher get() = Cipher.getInstance(TRANSFORMATION).apply {
//        init(Cipher.DECRYPT_MODE, getKey())
//    }
//
//    private fun getDecryptCipher(iv: ByteArray): Cipher {
//        return Cipher.getInstance(TRANSFORMATION).apply {
//            init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
//        }
//    }
//
//    private fun getKey(): SecretKey {
//        val existingKey = keyStore.getEntry("secret", null) as? KeyStore.SecretKeyEntry
//        return existingKey?.secretKey ?: createKey()
//    }
//
//    private fun createKey(): SecretKey {
//        return KeyGenerator.getInstance(ALGORITHM).apply {
//            init(
//                KeyGenParameterSpec.Builder(
//                    "secret",
//                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
//                )
//                    .setBlockModes(BLOCK_MODE)
//                    .setEncryptionPaddings(PADDING)
//                    .setUserAuthenticationRequired(false)
//                    .build()
//            )
//        }.generateKey()
//    }
//
//    private fun encrypt(data: ByteArray): Pair<ByteArray, ByteArray> {
//        val cipher = encryptCipher
//        val encryptedData = cipher.doFinal(data)
//        return Pair(encryptedData, cipher.iv)
//    }
//
//    private fun decrypt(data: ByteArray, iv: ByteArray): ByteArray {
//        val cipher = getDecryptCipher(iv)
//        return cipher.doFinal(data)
//    }
//
//    fun encryptString(data: String): Pair<String, String> {
//        val byteArray = data.toByteArray(Charsets.UTF_8)
//        val (encryptedData, iv) = encrypt(byteArray)
//        return Pair(Base64.encodeToString(encryptedData, Base64.DEFAULT), Base64.encodeToString(iv, Base64.DEFAULT))
//    }
//
//    fun decryptString(encryptedData: String, iv: String): String {
//        val byteArray = Base64.decode(encryptedData, Base64.DEFAULT)
//        val ivArray = Base64.decode(iv, Base64.DEFAULT)
//        val decryptedData = decrypt(byteArray, ivArray)
//        return String(decryptedData, Charsets.UTF_8)
//    }
//
//    fun encryptDouble(data: Double): Pair<String, String> {
//        val byteArray = ByteBuffer.allocate(8).putDouble(data).array()
//        val (encryptedData, iv) = encrypt(byteArray)
//        return Pair(Base64.encodeToString(encryptedData, Base64.DEFAULT), Base64.encodeToString(iv, Base64.DEFAULT))
//    }
//
//    fun decryptDouble(encryptedData: String, iv: String): Double {
//        val byteArray = Base64.decode(encryptedData, Base64.DEFAULT)
//        val ivArray = Base64.decode(iv, Base64.DEFAULT)
//        val decryptedData = decrypt(byteArray, ivArray)
//        return ByteBuffer.wrap(decryptedData).double
//    }
//
//    companion object {
//        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
//        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
//        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
//
//        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
//    }
//}

