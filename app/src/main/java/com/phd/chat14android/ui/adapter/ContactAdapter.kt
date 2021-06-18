package com.phd.chat14android.ui.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.phd.chat14android.R
import com.phd.chat14android.data.models.User
import com.phd.chat14android.ui.MessageActivity
import de.hdodenhof.circleimageview.CircleImageView

class ContactAdapter(private val listener:OnItemClickListener)
    : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    var userList: List<User>? = null


    fun getContactList(userList: List<User>?) {
        this.userList = userList as List<User>
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.contact_item_layout, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val item = userList?.get(position)
        holder.bind(item!!)
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, MessageActivity::class.java)
            intent.putExtra("hisId", item.uid)
            intent.putExtra("hisImage", item.profileImageUrl)
            it.context.startActivity(intent)
        }

        holder.addFriendView.setOnClickListener(object:View.OnClickListener { // use callback function in the place you want
            override fun onClick(v: View?) {
                if (listener != null)
                listener.onItemClick(position)
                print("Item $position clicked")
            }
        })
    }

    override fun getItemCount(): Int {
        return userList!!.size
    }


    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {


        val circleImageView = itemView.findViewById<CircleImageView>(R.id.imgContactUserInfo)
        val addFriendView = itemView.findViewById<CircleImageView>(R.id.imgContact)
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


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}