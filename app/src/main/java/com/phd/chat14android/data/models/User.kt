package com.phd.chat14android.data.models

import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView


data class User(
    var uid: String? = null,
    var name: String? = null,
    var profileImageUrl: String? = null,
    val status: String = "",
    val online: String = "offline",
    val typing: String = "false"
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


