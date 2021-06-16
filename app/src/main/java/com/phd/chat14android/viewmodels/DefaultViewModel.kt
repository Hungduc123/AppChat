package com.phd.chat14android.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.phd.chat14android.models.Event
import com.phd.chat14android.data.Result

abstract class DefaultViewModel : ViewModel() {
    protected val mSnackBarText = MutableLiveData<Event<String>>()
    val snackBarText: LiveData<Event<String>> = mSnackBarText

    private val mDataLoading = MutableLiveData<Event<Boolean>>()
    val dataLoading: LiveData<Event<Boolean>> = mDataLoading

    protected fun <T> onResult(mutableLiveData: MutableLiveData<T>? = null, result: Result<T>) {
        when (result) {
            is Result.Loading -> mDataLoading.value = Event(true)

            is Result.Error -> {
                mDataLoading.value = Event(false)
                result.msg?.let { mSnackBarText.value = Event(it) }
            }

            is Result.Success -> {
                mDataLoading.value = Event(false)
                result.data?.let { mutableLiveData?.value = it }
                result.msg?.let { mSnackBarText.value = Event(it) }
            }
        }
    }
}