package com.phd.chat14android.data.repository

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.phd.chat14android.data.models.User
import com.phd.chat14android.util.AppUtil
import java.util.*

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
    fun updateUID(uid: String?) {


        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("users").child(appUtil.getUID()!!)

        val map = mapOf<String, Any>("uid" to uid!!)
        databaseReference.updateChildren(map)

    }

    fun updateName(userName: String?) {


        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("users").child(appUtil.getUID()!!)

        val map = mapOf<String, Any>("name" to userName!!)
        databaseReference.updateChildren(map)

    }

    fun updateStatus(status: String){
        val databaseReference:DatabaseReference = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(appUtil.getUID()!!)

        val map:Map<String,Any> = mapOf<String,Any>("status" to status)
        databaseReference.updateChildren(map)
    }

    fun updateImagePath(imagePath: String) {
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("users").child(appUtil.getUID()!!)

        val map = mapOf<String, Any>("profileImageUrl" to imagePath)
        databaseReference.updateChildren(map)
    }

    fun uploadImageToFirebaseStorage(imageUri: Uri){

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(imageUri)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Successfully uploaded image: ${it.metadata?.path}")

                @Suppress("NestedLambdaShadowedImplicitParameter")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d(ContentValues.TAG, "File Location: $it")
                    //saveUserToFirebaseDatabase(it.toString())
                    updateImagePath(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d(ContentValues.TAG, "Failed to upload image to storage: ${it.message}")

            }
    }


}