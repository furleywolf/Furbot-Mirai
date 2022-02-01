package cn.transfur.furbot

import java.security.MessageDigest

private val messageDigest: MessageDigest = MessageDigest.getInstance("MD5")

fun buildSignString(
    apiPath: String,
    timestamp: Long,
    authKey: String
): String {
    val original = "$apiPath-$timestamp-$authKey"
    return messageDigest.digest(original.toByteArray())
        .joinToString(separator = "") { byte ->
            byte.toUByte().toString(16).let { s ->
                if (s.length > 1) s else "0$s"
            }
        }
}