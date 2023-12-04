package com.example.gossip.activity

import android.text.TextUtils
import android.util.Patterns

class ValidationUtils {

    fun isValidUsername(username : String) : Boolean {
        return TextUtils.isEmpty(username)
    }

    fun isValidEmail(email: String) : Boolean {
        return TextUtils.isEmpty(email)
    }

    fun isValidEmailPattern(email : String) : Boolean {
        return !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password : String) : Boolean {
        return (TextUtils.isEmpty(password))
    }

    fun isValidPasswordLength(password: String) : Boolean {
        return password.length < 8
    }

    fun isValidConfirmPassword(password : String, confirmPassword : String) : Boolean {
        return password != confirmPassword
    }
 }