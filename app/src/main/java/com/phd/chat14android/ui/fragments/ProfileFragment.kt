package com.phd.chat14android.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.phd.chat14android.R
import com.phd.chat14android.databinding.FragmentProfileBinding
import com.phd.chat14android.viewmodels.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    private lateinit var binding:FragmentProfileBinding
    private lateinit var viewModels: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false)

        viewModels = ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application)
            .create(ProfileViewModel::class.java)

        viewModels.getUser().observe(viewLifecycleOwner, Observer {
            binding.userModel = it
            binding.username.text = it.name

        })
        return binding.root
    }

}