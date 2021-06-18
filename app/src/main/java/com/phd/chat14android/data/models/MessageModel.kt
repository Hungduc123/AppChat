package com.phd.chat14android.data.models

import android.widget.ImageView
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

class MessageModel constructor(
    var senderId: String = "",
    var receiverId: String = "",
    @Bindable var message: String = "",
    var date: String = "",
    var type: String = ""

): BaseObservable() {
    companion object {

        @JvmStatic
        @BindingAdapter("imageMessage")
        public fun loadImage(imageView: ImageView, image: String?) {
            if (image != null) {
                Glide.with(imageView.context).load(image).into(imageView)
            }
        }
    }
}