package com.phd.chat14android.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.phd.chat14android.R
import com.phd.chat14android.databinding.FragmentProfileBinding
import com.phd.chat14android.models.User
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    private lateinit var binding:FragmentProfileBinding
    private lateinit var reference: DatabaseReference
    private lateinit var fUser : FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false)
        fUser = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("users").child(fUser.uid)

        reference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange( snapshot: DataSnapshot) {
                val user= snapshot.getValue(User::class.java)
                username.text = user?.name
                if (user?.profileImageUrl == null){
                    profile_image.setImageResource(R.drawable.ic_user)
                }else{
                    Glide.with(context!!).load(user.profileImageUrl).into(profile_image)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        return binding.root
    }

}