package com.phd.chat14android.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.phd.chat14android.R
import com.phd.chat14android.data.models.ContactUser
import de.hdodenhof.circleimageview.CircleImageView


class DetailsDialogue : DialogFragment() {

    private val userList: List<ContactUser>? = null
    private val position = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_dialogue, container, false)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.fragment_details_dialogue, null)
        builder.setView(view).setTitle("Contact Details").setIcon(R.drawable.ic_user)
            .setCancelable(true)
            .setNegativeButton("Close",
                DialogInterface.OnClickListener { dialog, which -> })
        val circleImageView = view.findViewById<CircleImageView>(R.id.detailsImageId)
        val nameTextView = view.findViewById<TextView>(R.id.detailsNameId)
        val statusTextView = view.findViewById<TextView>(R.id.txtProfileStatus)
        Glide.with(view.context).load(userList!![position].contactImage).centerCrop()
            .placeholder(R.drawable.ic_user).into(circleImageView)
        nameTextView.setText("Name: " + userList!![position].contactName)
        statusTextView.setText("Phone: " + userList!![position].contactStatus)
        return builder.create()
    }

}