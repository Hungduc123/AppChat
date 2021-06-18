package com.phd.chat14android.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.QuerySnapshot
import com.phd.chat14android.data.models.ContactUser
import com.phd.chat14android.data.models.User
import java.util.*


@Suppress("UNCHECKED_CAST")
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

    fun insertContactFirebase(user: User) {


        val currentUser = firebaseAuth.currentUser!!.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("ContactList").child(currentUser)
                val contactMap: MutableMap<String, String?> =
                    HashMap()
                contactMap["uid"] = user.uid
                contactMap["name"] = user.name
                contactMap["profileImageUrl"] = user.profileImageUrl


        databaseReference.updateChildren(contactMap as Map<String, Any>).addOnSuccessListener {
            print("Adding successs")
        }

    }

    fun getContactsFromFirebase(): MutableLiveData<List<User>> {
        val currentUser = firebaseAuth.currentUser!!.uid
        val contactList: MutableList<User> = ArrayList<User>()
        val getContactsMutableLiveData: MutableLiveData<List<User>> = MutableLiveData<List<User>>()
            databaseReference = FirebaseDatabase.getInstance().getReference("ContactList")
        val query:Query = databaseReference.orderByChild(currentUser)
                query.addValueEventListener(object:ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (snapshot1 in snapshot.children) {
                        val user = snapshot1.getValue(User::class.java)
                        contactList.add(user!!)
                    }
                    getContactsMutableLiveData.value = contactList
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return getContactsMutableLiveData
    }




    fun searchDataFirebase(s: String?):MutableLiveData<List<User>>{

        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        val query : Query = databaseReference.orderByChild("name").equalTo(s)
        val searchList: MutableList<User> = ArrayList<User>()
        val getSearchMutableLiveData: MutableLiveData<List<User>> = MutableLiveData<List<User>>()
        query.addValueEventListener(object:ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (snapshot1 in snapshot.children) {
                        val user = snapshot1.getValue(User::class.java)
                        searchList.add(user!!)
                    }
                    getSearchMutableLiveData.value = searchList
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return getSearchMutableLiveData
    }
}
