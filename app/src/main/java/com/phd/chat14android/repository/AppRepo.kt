package com.phd.chat14android.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.phd.chat14android.models.User
import com.phd.chat14android.util.AppUtil

class AppRepo {
    private var liveData:MutableLiveData<User>?= null
    private lateinit var databaseReference:DatabaseReference
    private val appUtil = AppUtil()

    object StaticFunction{
        private var instance: AppRepo? = null
        fun getInstance():AppRepo{
            if (instance == null)
                instance = AppRepo()
            return instance!!
        }
    }

     fun getUser():LiveData<User>{
        if (liveData == null)
            liveData = MutableLiveData()
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(appUtil.getUID()!!)

            databaseReference.addValueEventListener(object:ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val user = snapshot.getValue(User::class.java)
                        liveData!!.postValue(user)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
            return liveData!!
    }
}