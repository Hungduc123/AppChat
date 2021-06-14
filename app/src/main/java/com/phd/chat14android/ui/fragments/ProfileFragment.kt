package com.phd.chat14android.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.phd.chat14android.R
import com.phd.chat14android.databinding.FragmentProfileBinding
import com.phd.chat14android.ui.EditNameActivity
import com.phd.chat14android.viewmodels.ProfileViewModel
import kotlinx.android.synthetic.main.dialog_layout.view.*


class ProfileFragment : Fragment() {

    private val TAG = ProfileFragment::class.java.simpleName
    private lateinit var dialog: AlertDialog
    private lateinit var binding:FragmentProfileBinding
    private lateinit var viewModels: ProfileViewModel
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false)

        sharedPreferences = requireContext().getSharedPreferences("userData", Context.MODE_PRIVATE)
        viewModels = ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application)
            .create(ProfileViewModel::class.java)


        viewModels.getUser().observe(viewLifecycleOwner, Observer { userModel->
            binding.userModel = userModel

            binding.username.text = userModel.name
            if (userModel.name?.contains(" ")!!){
                var split = userModel.name?.split(" ")
                binding.txtProfileFName.text = split!![0]
                binding.txtProfileLName.text = split[1]
            }

            //Update status
            binding.txtProfileStatus.setOnClickListener{
                getStatusDialog()
            }

            //Update Name
            binding.cardNamme.setOnClickListener {
                val intent = Intent(context, EditNameActivity::class.java)
                intent.putExtra("name",userModel.name)
                startActivityForResult(intent, 100)

            }

        })


        return binding.root
    }

    private fun getStatusDialog() {

        val alertDialog = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null, false)
        alertDialog.setView(view)

        view.btnEditStatus.setOnClickListener {
            val status = view.edtUserStatus.text.toString()
            if (status.isNotEmpty()) {
                viewModels.updateStatus(status)
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
                    viewModels.updateName(userName!!)
                    val editor = sharedPreferences.edit()
                    editor.putString("myName", userName).apply()
                }

            }
        }
    }
}