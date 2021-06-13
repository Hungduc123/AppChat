package com.phd.chat14android.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.google.firebase.auth.FirebaseAuth
import com.phd.chat14android.MainActivity
import com.phd.chat14android.R
import com.phd.chat14android.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val TAG = LoginActivity::class.java.simpleName
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        Glide.with(this).asGif()
            .load("https://media1.tenor.com/images/1d550cc7494b9ac5a85fbe4f6bc184c8/tenor.gif?itemid=11525834")
            .apply(RequestOptions.circleCropTransform())
            .into(binding.kotlinImageView)
        binding.loginButtonLogin.setOnClickListener {
            performLogin()
        }

        binding.backToRegisterTextview.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun performLogin() {
        val email = binding.emailEdittextLogin.text.toString()
        val password = binding.passwordEdittextLogin.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        binding.backToRegisterTextview.visibility = View.GONE
        binding.loadingView.visibility = View.VISIBLE

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                Log.d(TAG, "Successfully logged in: ${it.result!!.user?.uid}")

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                overridePendingTransition(R.anim.enter, R.anim.exit)
            }
            .addOnFailureListener {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()

                binding.backToRegisterTextview.visibility = View.VISIBLE
                binding.loadingView.visibility = View.GONE
            }
    }
}