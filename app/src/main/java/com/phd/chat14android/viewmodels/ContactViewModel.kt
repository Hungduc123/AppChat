package com.phd.chat14android.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.phd.chat14android.data.models.ContactUser
import com.phd.chat14android.data.models.User
import com.phd.chat14android.data.repository.ContactRepository

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ContactRepository = ContactRepository.StaticFunction.getInstance()

    var getContactLiveData: LiveData<List<User>>? = null
    var searchLiveData: LiveData<List<User>>? = null


    fun search(s: String?) {
        searchLiveData = repository.searchDataFirebase(s)
    }

}