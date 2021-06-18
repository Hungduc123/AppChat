package com.phd.chat14android.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.phd.chat14android.data.models.CreateUser
import com.phd.chat14android.data.repository.AuthRepository
import com.phd.chat14android.models.Event
import com.phd.chat14android.data.Result
import com.phd.chat14android.data.models.User
import com.phd.chat14android.data.repository.AppRepo

class CreateAccountViewModel : DefaultViewModel() {

//    private val dbRepository = DatabaseRepository()
    private var authRepository = AuthRepository.StaticFunction.getInstance()
    private var appRepo = AppRepo.StaticFunction.getInstance()

    private val mIsCreatedEvent = MutableLiveData<Event<FirebaseUser>>()

    val isCreatedEvent: LiveData<Event<FirebaseUser>> = mIsCreatedEvent
    val displayNameText = MutableLiveData<String>()
    val emailText = MutableLiveData<String>()
    val passwordText = MutableLiveData<String>()
    val isCreatingAccount = MutableLiveData<Boolean>()

    private fun createAccount() {
        isCreatingAccount.value = true
        val createUser =
            CreateUser(displayNameText.value!!, emailText.value!!, passwordText.value!!)

        authRepository.createUser(createUser) { result: Result<FirebaseUser> ->
            onResult(null, result)
            if (result is Result.Success) {
                mIsCreatedEvent.value = Event(result.data!!)
                appRepo.updateName(createUser.displayName)
                val uid = FirebaseAuth.getInstance().uid ?: return@createUser
                appRepo.updateUID(uid)
            }
            if (result is Result.Success || result is Result.Error) isCreatingAccount.value = false
        }
    }

    fun createAccountPressed() {

        createAccount()
    }
}