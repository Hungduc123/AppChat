package com.phd.chat14android.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.phd.chat14android.ui.MessageActivity
import com.phd.chat14android.util.AppUtil
import java.util.*
import kotlin.collections.HashMap

class FirebaseNotificationService : FirebaseMessagingService() {

    private val appUtil = AppUtil()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        updateToken(token)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.data.isNotEmpty()) {

            val map: Map<String, String> = remoteMessage.data

            val title = map["title"]
            val message = map["message"]
            val hisId = map["hisId"]
            val hisImage = map["hisImage"]
            val chatId = map["chatId"]


        }


    }


    private fun updateToken(token: String) {

        val databaseReference =
            FirebaseDatabase.getInstance().getReference("users").child(appUtil.getUID()!!)
        val map: MutableMap<String, Any> = HashMap()
        map["token"] = token
        databaseReference.updateChildren(map)

    }






}