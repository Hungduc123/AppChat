package com.phd.chat14android.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.phd.chat14android.models.User
import com.phd.chat14android.repository.AppRepo

class ProfileViewModel : ViewModel() {

    private var appRepo = AppRepo.StaticFunction.getInstance()


     fun getUser() : LiveData<User> {
        return appRepo.getUser()
    }

    fun updateName(userName: String?) {
        appRepo.updateName(userName!!)
    }

    fun updateStatus(status:String){
        appRepo.updateStatus(status)
    }

}