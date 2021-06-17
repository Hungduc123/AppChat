package com.phd.chat14android.models

import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView


data class User(
    var uid: String? = null,
    var name: String? = null,
    var profileImageUrl: String? = null,
    var status: String = "",
    var online: String = "offline"
){

    companion object {

        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: CircleImageView, imageUrl: String?) {
            imageUrl?.let {
                Glide.with(view.context).load(imageUrl).into(view)
            }
        }
    }

}


