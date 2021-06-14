package com.phd.chat14android.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AppUtil {
    public fun getUID(): String? {
        val firebaseAuth = FirebaseAuth.getInstance()
        return firebaseAuth.uid
    }

    fun updateOnlineStatus(status: String) {

        val databaseReference =
            FirebaseDatabase.getInstance().getReference("users").child(getUID()!!)
        val map = HashMap<String, Any>()
        map["online"] = status
        databaseReference.updateChildren(map)
    }
}