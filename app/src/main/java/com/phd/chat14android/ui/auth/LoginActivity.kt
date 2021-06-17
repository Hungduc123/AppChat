package com.phd.chat14android.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.phd.chat14android.MainActivity
import com.phd.chat14android.R
import com.phd.chat14android.databinding.ActivityLoginBinding
import com.phd.chat14android.models.EventObserver
import com.phd.chat14android.viewmodels.LoginViewModel
import java.util.*

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)

        viewModel = ViewModelProvider.AndroidViewModelFactory
            .getInstance(application)
            .create(LoginViewModel::class.java)
        binding.viewmodel = viewModel

        setupObservers()

        Glide.with(this).asGif()
            .load("https://media1.tenor.com/images/1d550cc7494b9ac5a85fbe4f6bc184c8/tenor.gif?itemid=11525834")
            .apply(RequestOptions.circleCropTransform())
            .into(binding.kotlinImageView)


        binding.backToRegisterTextview.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }
    private fun setupObservers() {
//       viewModel.dataLoading.observe(EventObserver){
//
//       }
//
//        viewModel.snackBarText.observe(viewLifecycleOwner,
//            EventObserver { text ->
//                view?.showSnackBar(text)
//                view?.forceHideKeyboard()
//            })

        viewModel.isLoggedInEvent.observe(this, EventObserver {

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            overridePendingTransition(R.anim.enter, R.anim.exit)
        })
    }



}