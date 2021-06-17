package com.phd.chat14android.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.phd.chat14android.data.models.ContactUser
import com.phd.chat14android.data.models.User
import java.util.*


class ContactRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var databaseReference: DatabaseReference
    private var liveData:LiveData<List<ContactUser>>?= null


    object StaticFunction{
        private var instance: ContactRepository? = null
        fun getInstance():ContactRepository{
            if (instance == null)
                instance = ContactRepository()
            return instance!!
        }
    }




    fun searchDataFirebase(s: String?):MutableLiveData<List<User>>{

        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        val searchList: MutableList<User> = ArrayList<User>()
        val getSearchMutableLiveData: MutableLiveData<List<User>> = MutableLiveData<List<User>>()
        databaseReference.addValueEventListener(object:ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (snapshot1 in snapshot.children) {
                        val user = snapshot1.getValue(User::class.java)
                        searchList.add(user!!)
                    }
                    getSearchMutableLiveData.setValue(searchList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return getSearchMutableLiveData
    }
}
