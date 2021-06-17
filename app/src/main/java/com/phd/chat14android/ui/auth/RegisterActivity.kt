package com.phd.chat14android.ui.auth

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.phd.chat14android.MainActivity
import com.phd.chat14android.R
import com.phd.chat14android.databinding.ActivityRegisterBinding
import com.phd.chat14android.data.models.User
import com.phd.chat14android.models.EventObserver
import com.phd.chat14android.viewmodels.CreateAccountViewModel
import com.phd.chat14android.viewmodels.LoginViewModel
import com.squareup.picasso.Picasso
import java.util.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel : CreateAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        viewModel = ViewModelProvider.AndroidViewModelFactory
            .getInstance(application)
            .create(CreateAccountViewModel::class.java)
        binding.viewmodel = viewModel


        setupObservers()

        binding.alreadyHaveAccountTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.enter, R.anim.exit)
        }


    }

    private fun setupObservers() {
        viewModel.dataLoading.observe(this,
            EventObserver {  })



        viewModel.isCreatedEvent.observe(this, EventObserver {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            overridePendingTransition(R.anim.enter, R.anim.exit)
        })
    }

}