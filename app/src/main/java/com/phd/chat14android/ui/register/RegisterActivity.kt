package com.phd.chat14android.ui.register

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.phd.chat14android.MainActivity
import com.phd.chat14android.R
import com.phd.chat14android.databinding.ActivityRegisterBinding
import com.phd.chat14android.models.User
import com.phd.chat14android.ui.login.LoginActivity
import com.squareup.picasso.Picasso
import java.util.*


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var selectedPhotoUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)


        binding.registerButtonRegister.setOnClickListener {
           performRegistration()
            Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
        }

        binding.alreadyHaveAccountTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.enter, R.anim.exit)
        }

        binding.selectphotoButtonRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data ?: return
            Log.d(TAG, "Photo was selected")
            // Get and resize profile image
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            contentResolver.query(selectedPhotoUri!!, filePathColumn, null, null, null)?.use {
                it.moveToFirst()
                val columnIndex = it.getColumnIndex(filePathColumn[0])
                val picturePath = it.getString(columnIndex)
                // If picture chosen from camera rotate by 270 degrees else
                if (picturePath.contains("DCIM")) {
                    Picasso.get().load(selectedPhotoUri).rotate(270f).into(binding.selectphotoImageviewRegister)
                } else {
                    Picasso.get().load(selectedPhotoUri).into(binding.selectphotoImageviewRegister)
                }
            }
            binding.selectphotoButtonRegister.alpha = 0f
        }
    }

    private fun performRegistration() {
        val email = binding.emailEdittextRegister.text.toString()
        val password = binding.passwordEdittextRegister.text.toString()
        val name = binding.nameEdittextRegister.text.toString()

        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedPhotoUri == null) {
            Toast.makeText(this, "Please select a photo", Toast.LENGTH_SHORT).show()
            return
        }

        binding.alreadyHaveAccountTextView.visibility = View.GONE
        binding.loadingView.visibility = View.VISIBLE

        // Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // else if successful
                binding.loadingView.visibility = View.GONE
                binding.alreadyHaveAccountTextView.visibility = View.VISIBLE

                Log.d("TAG", "Successfully created user with uid: ${it.result!!.user?.uid}")
                uploadImageToFirebaseStorage()

            }
            .addOnFailureListener {
                Log.d("TAG", "Failed to create user: ${it.message}")
                binding.loadingView.visibility = View.GONE
                binding.alreadyHaveAccountTextView.visibility = View.VISIBLE
                Toast.makeText(this, "${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) {
            // save user without photo
            saveUserToFirebaseDatabase(null)
        } else {
            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")

                    @Suppress("NestedLambdaShadowedImplicitParameter")
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d(TAG, "File Location: $it")
                        saveUserToFirebaseDatabase(it.toString())
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to upload image to storage: ${it.message}")
                    binding.loadingView.visibility = View.GONE
                    binding.alreadyHaveAccountTextView.visibility = View.VISIBLE
                }
        }

    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String?) {
        val uid = FirebaseAuth.getInstance().uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = if (profileImageUrl == null) {
            User(uid,binding.nameEdittextRegister.text.toString(), "")
        } else {
            User(uid, binding.nameEdittextRegister.text.toString(), profileImageUrl)
        }

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(TAG, "Finally we saved the user to Firebase Database")

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                overridePendingTransition(R.anim.enter, R.anim.exit)
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to set value to database: ${it.message}")
                binding.loadingView.visibility = View.GONE
                binding.alreadyHaveAccountTextView.visibility = View.VISIBLE
            }
    }
}