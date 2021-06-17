package com.phd.chat14android.ui.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.Layout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.storage.FirebaseStorage
import com.phd.chat14android.R
import com.phd.chat14android.databinding.FragmentProfileBinding
import com.phd.chat14android.ui.EditNameActivity
import com.phd.chat14android.viewmodels.ProfileViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.dialog_layout.view.*
import java.util.*


class  ProfileFragment : Fragment() {

    private val TAG = ProfileFragment::class.java.simpleName
    private lateinit var dialog: AlertDialog
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        sharedPreferences = requireContext().getSharedPreferences("userData", Context.MODE_PRIVATE)
        viewModel = ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application)
            .create(ProfileViewModel::class.java)


        viewModel.getUser().observe(viewLifecycleOwner, Observer { userModel ->
            binding.userModel = userModel

            binding.username.text = userModel.name
            if (userModel.name?.contains(" ")!!) {
                var split = userModel.name?.split(" ")
                binding.txtProfileFName.text = split!![0]
                binding.txtProfileLName.text = split[1]
            }

            //Update status
            binding.txtProfileStatus.setOnClickListener {
                getStatusDialog()
            }

            //Update Name
            binding.cardNamme.setOnClickListener {
                val intent = Intent(context, EditNameActivity::class.java)
                intent.putExtra("name", userModel.name)
                startActivityForResult(intent, 100)
            }

        }) // end of  viewModel.getUser().observe

        binding.profileImage.setOnClickListener {
            pickImage()
        }

        return binding.root
    }

    private fun pickImage() {
        CropImage.activity().setCropShape(CropImageView.CropShape.OVAL)
            .start(requireContext(), this)
    }

    private fun pickImage() {
        CropImage.activity().setCropShape(CropImageView.CropShape.OVAL)
            .start(requireContext(), this)
    }

    private fun getStatusDialog() {

        val alertDialog = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null, false)
        alertDialog.setView(view)

        view.btnEditStatus.setOnClickListener {
            val status = view.edtUserStatus.text.toString()
            if (status.isNotEmpty()) {
                viewModel.updateStatus(status)
                dialog.dismiss()
            }
        }
        dialog = alertDialog.create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            100 -> {
                if (data != null) {
                    val userName = data.getStringExtra("name")
                    viewModel.updateName(userName!!)
                    val editor = sharedPreferences.edit()
                    editor.putString("myName", userName).apply()
                }
            } //end of requescode = 100

            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                if (data != null) {
                    val result = CropImage.getActivityResult(data)
                    if (resultCode == Activity.RESULT_OK) {
                        viewModel.updateImage(result.uri)
                    }
                }
            }
        }
    }
}