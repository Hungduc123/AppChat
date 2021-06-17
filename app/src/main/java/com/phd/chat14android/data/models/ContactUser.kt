package com.phd.chat14android.data.models

import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import java.io.Serializable

class ContactUser(var contactId: String? = null,
                  var contactName: String? = null,
                  var contactImage: String? = null,
                  var contactStatus: String? = null,
                  var contactEmail: String? = null)

     {

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