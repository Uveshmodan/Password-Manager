package com.chaintech.passwordmanager.utils

import java.nio.charset.Charset
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object EncryptionUtil {
    // AES encryption algorithm
    private const val ALGORITHM = "AES"

    // AES/CBC/PKCS5Padding transformation
    private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"

    // 16 bytes encryption key
    private val key = "1234567890123456".toByteArray(Charset.forName("UTF-8"))

    // 16 bytes initialization vector (IV)
    private val iv = "abcdefghijklmnop".toByteArray(Charset.forName("UTF-8"))

    // Encrypts the input string using AES encryption
    fun encrypt(input: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val secretKeySpec = SecretKeySpec(key, ALGORITHM)
        val ivParameterSpec = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)
        val encryptedBytes = cipher.doFinal(input.toByteArray(Charset.forName("UTF-8")))
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    // Decrypts the input string using AES decryption
    fun decrypt(input: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val secretKeySpec = SecretKeySpec(key, ALGORITHM)
        val ivParameterSpec = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)
        val decodedBytes = Base64.getDecoder().decode(input)
        val decryptedBytes = cipher.doFinal(decodedBytes)
        return String(decryptedBytes, Charset.forName("UTF-8"))
    }
}
