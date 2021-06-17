package com.phd.chat14android.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.phd.chat14android.R
import com.phd.chat14android.data.models.User
import de.hdodenhof.circleimageview.CircleImageView

class ContactAdapter: RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    var userList: List<User>? = null
    private var clickInterface: ClickInterface? = null

    fun getContactList(userList: List<User>?) {
        this.userList = userList as List<User>
    }

    fun ContactAdapter(clickInterface: ClickInterface?) {
        this.clickInterface = clickInterface
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.contact_item_layout, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val item = userList?.get(position)
        holder.bind(item!!)
    }

    override fun getItemCount(): Int {
        return userList!!.size
    }


    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val circleImageView = itemView.findViewById<CircleImageView>(R.id.imgContact)
        val contactName = itemView.findViewById<TextView>(R.id.txtContactName)
        val contactStatus = itemView.findViewById<TextView>(R.id.txtContactStatus)
        fun bind(item: User) {
            contactName.text = item.name
            contactStatus.text = item.status
            Glide.with(itemView.context)
                .load(item.profileImageUrl)
                .into(circleImageView)
        }
    }


    interface ClickInterface {
        // for on Click....
        fun onItemClick(position: Int)
        fun onLongItemClick(position: Int)
    }
}