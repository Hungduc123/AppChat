package com.phd.chat14android.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.phd.chat14android.R
import com.phd.chat14android.data.models.User
import com.phd.chat14android.databinding.FragmentContactsBinding
import com.phd.chat14android.ui.MessageActivity
import com.phd.chat14android.ui.adapter.ContactAdapter
import com.phd.chat14android.viewmodels.ContactViewModel
import dmax.dialog.SpotsDialog
import java.util.*


class ContactsFragment : Fragment(){

    private lateinit var viewModel: ContactViewModel
    private var userList: List<User> = ArrayList<User>()
    private var adapter: ContactAdapter? = null
    private lateinit var binding:FragmentContactsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_contacts,container,false)

        viewModel = ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application)
            .create(ContactViewModel::class.java)

        setUpRecycle()

        adapter = ContactAdapter()
        binding.recycleViewId.setHasFixedSize(true)
        binding.recycleViewId.layoutManager = LinearLayoutManager(activity)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchViewId.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                val dialogue = SpotsDialog.Builder().setContext(activity).setTheme(R.style.Custom)
                    .setCancelable(true).build()
                dialogue.show()
                viewModel.search(s)
                viewModel.searchLiveData?.observe(
                    activity!!, { User ->
                        dialogue.dismiss()
                        userList =User as List<User>
                        adapter!!.getContactList(userList)
                        binding.recycleViewId.adapter = adapter
                    })
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

    }

    private fun setUpRecycle() {
        val dialogue: AlertDialog =
            SpotsDialog.Builder().setContext(activity).setTheme(R.style.Custom).setCancelable(true).build()
        dialogue.show()
        //viewModel.show()
        viewModel.getContactLiveData?.observe( requireActivity(), Observer  { User ->
            dialogue.dismiss()
            userList = User as List<User>
            //userList.clear();
            adapter?.getContactList(userList)
            binding.recycleViewId.adapter = adapter
        })
    }



}