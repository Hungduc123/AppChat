package com.phd.chat14android.data.repository

import com.google.firebase.auth.FirebaseUser
import com.phd.chat14android.data.models.CreateUser
import com.phd.chat14android.data.models.Login
import com.phd.chat14android.data.remote.FirebaseAuthSource
import com.phd.chat14android.data.remote.FirebaseAuthStateObserver

class AuthRepository{
    private val firebaseAuthService = FirebaseAuthSource()

    object StaticFunction{
        private var instance: AuthRepository? = null
        fun getInstance():AuthRepository{
            if (instance == null)
                instance = AuthRepository()
            return instance!!
        }
    }

    fun observeAuthState(stateObserver: FirebaseAuthStateObserver, b: ((com.phd.chat14android.data.Result<FirebaseUser>) -> Unit)){
        firebaseAuthService.attachAuthStateObserver(stateObserver,b)
    }

    fun loginUser(login: Login, b: ((com.phd.chat14android.data.Result<FirebaseUser>) -> Unit)) {
        b.invoke(com.phd.chat14android.data.Result.Loading)
        firebaseAuthService.loginWithEmailAndPassword(login).addOnSuccessListener {
            b.invoke(com.phd.chat14android.data.Result.Success(it.user))
        }.addOnFailureListener {
            b.invoke(com.phd.chat14android.data.Result.Error(msg = it.message))
        }
    }

    fun createUser(createUser: CreateUser, b: ((com.phd.chat14android.data.Result<FirebaseUser>) -> Unit)) {
        b.invoke(com.phd.chat14android.data.Result.Loading)
        firebaseAuthService.createUser(createUser).addOnSuccessListener {
            b.invoke(com.phd.chat14android.data.Result.Success(it.user))
        }.addOnFailureListener {
            b.invoke(com.phd.chat14android.data.Result.Error(msg = it.message))
        }
    }

    fun logoutUser() {
        firebaseAuthService.logout()
    }
}