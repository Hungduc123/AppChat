package com.phd.chat14android.util

import com.google.firebase.auth.FirebaseAuth

class AppUtil {
    public fun getUID(): String? {
        val firebaseAuth = FirebaseAuth.getInstance()
        return firebaseAuth.uid
    }
}