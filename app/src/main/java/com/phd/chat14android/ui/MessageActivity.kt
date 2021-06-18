package com.phd.chat14android.ui

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.phd.chat14android.data.models.ChatListModel
import com.phd.chat14android.data.models.MessageModel
import com.phd.chat14android.data.models.User
import com.phd.chat14android.databinding.ActivityMessageBinding
import com.phd.chat14android.databinding.LeftItemLayoutBinding
import com.phd.chat14android.databinding.RightItemLayoutBinding
import com.phd.chat14android.util.AppUtil
import org.json.JSONObject
import androidx.databinding.library.baseAdapters.BR


class MessageActivity : AppCompatActivity() {

    private lateinit var activityMessageBinding: ActivityMessageBinding
    private var hisId: String? = null
    private var hisImage: String? = null
    private var chatId: String? = null
    private var myName: String? = null
    private lateinit var appUtil: AppUtil
    private lateinit var myId: String
    private var firebaseRecyclerAdapter: FirebaseRecyclerAdapter<MessageModel, ViewHolder>? = null
    private lateinit var myImage: String
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        activityMessageBinding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(activityMessageBinding.root)
        appUtil = AppUtil()
        myId = appUtil.getUID()!!
        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE)
        myImage = sharedPreferences.getString("myImage", "").toString()
        myName = sharedPreferences.getString("myName", "").toString()




        activityMessageBinding.activity = this


        if (intent.hasExtra("chatId")) {

            chatId = intent.getStringExtra("chatId")
            hisId = intent.getStringExtra("hisId")
            hisImage = intent.getStringExtra("hisImage")
            readMessages(chatId!!)

        } else {
            hisId = intent.getStringExtra("hisId")
            hisImage = intent.getStringExtra("hisImage")
        }

        activityMessageBinding.hisImage = hisImage

        activityMessageBinding.btnSend.setOnClickListener {
            val message = activityMessageBinding.msgText.text.toString()
            if (message.isEmpty())
                Toast.makeText(this, "Enter Message", Toast.LENGTH_SHORT).show()
            else {
                sendMessage(message)

                getToken(message)

            }
        }

        activityMessageBinding.msgText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty())
                    typingStatus("false")
                else
                    typingStatus(hisId!!)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        activityMessageBinding.btnDataSend.setOnClickListener {
            //pickImage()
        }



        if (chatId == null)
            checkChat(hisId!!)

        checkOnlineStatus()

    }


    private fun checkChat(hisId: String) {

        val databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(myId)
        val query = databaseReference.orderByChild("member").equalTo(hisId)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (ds in snapshot.children) {
                        val member = ds.child("member").value.toString()
                        if (member == hisId) {
                            chatId = ds.key
                            readMessages(chatId!!)

                            break
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun createChat(message: String) {

        var databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(myId)
        chatId = databaseReference.push().key

        val chatListMode =
            ChatListModel(chatId!!, message, System.currentTimeMillis().toString(), hisId!!)

        databaseReference.child(chatId!!).setValue(chatListMode)

        databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(hisId!!)

        val chatList =
            ChatListModel(chatId!!, message, System.currentTimeMillis().toString(), myId)

        databaseReference.child(chatId!!).setValue(chatList)

        databaseReference = FirebaseDatabase.getInstance().getReference("Chat").child(chatId!!)

        val messageModel = MessageModel(myId, hisId!!, message, type = "text")
        databaseReference.push().setValue(messageModel)


    }

    private fun sendMessage(message: String) {

        if (chatId == null)
            createChat(message)
        else {

            var databaseReference =
                FirebaseDatabase.getInstance().getReference("Chat").child(chatId!!)

            val messageModel =
                MessageModel(myId, hisId!!, message, System.currentTimeMillis().toString(), "text")

            databaseReference.push().setValue(messageModel)

            val map: MutableMap<String, Any> = HashMap()

            map["lastMessage"] = message
            map["date"] = System.currentTimeMillis().toString()

            databaseReference =
                FirebaseDatabase.getInstance().getReference("ChatList").child(myId).child(chatId!!)

            databaseReference.updateChildren(map)

            databaseReference =
                FirebaseDatabase.getInstance().getReference("ChatList").child(hisId!!)

                    .child(chatId!!)
            databaseReference.updateChildren(map)
        }
    }

    private fun readMessages(chatId: String) {

        val query = FirebaseDatabase.getInstance().getReference("Chat").child(chatId)

        val firebaseRecyclerOptions = FirebaseRecyclerOptions.Builder<MessageModel>()
            .setLifecycleOwner(this)
            .setQuery(query, MessageModel::class.java)
            .build()
        query.keepSynced(true)

        firebaseRecyclerAdapter =
            object : FirebaseRecyclerAdapter<MessageModel, ViewHolder>(firebaseRecyclerOptions) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

                    var viewDataBinding: ViewDataBinding? = null

                    if (viewType == 0)
                        viewDataBinding = RightItemLayoutBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                        )

                    if (viewType == 1)

                        viewDataBinding =LeftItemLayoutBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                        )

                    return ViewHolder(viewDataBinding!!)

                }

                override fun onBindViewHolder(
                    holder: ViewHolder,
                    position: Int,
                    messageModel: MessageModel
                ) {
                    if (getItemViewType(position) == 0) {
                        holder.viewDataBinding.setVariable(BR.message, messageModel)
                        holder.viewDataBinding.setVariable(BR.messageImage, myImage)
                    }

                    if (getItemViewType(position) == 1) {

                        holder.viewDataBinding.setVariable(BR.message, messageModel)
                        holder.viewDataBinding.setVariable(BR.messageImage, hisImage)
                    }
                }

                override fun getItemViewType(position: Int): Int {

                    val messageModel = getItem(position)
                    return if (messageModel.senderId == myId)
                        0
                    else
                        1
                }
            }

        activityMessageBinding.messageRecyclerView.layoutManager = LinearLayoutManager(this)
        activityMessageBinding.messageRecyclerView.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter!!.startListening()

    }

    class ViewHolder(var viewDataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root)

    override fun onPause() {
        super.onPause()
        if (firebaseRecyclerAdapter != null)
            firebaseRecyclerAdapter!!.stopListening()
        appUtil.updateOnlineStatus("offline")
    }

//    fun userInfo() {
//        val intent = Intent(this, UserInfoActivity::class.java)
//        intent.putExtra("userId", hisId)
//        startActivity(intent)
//    }

    override fun onResume() {
        super.onResume()
        appUtil.updateOnlineStatus("online")
    }

    private fun checkOnlineStatus() {

        val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(hisId!!)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userModel = snapshot.getValue(User::class.java)
                    activityMessageBinding.online = userModel?.online

                    val typing = userModel?.typing

                    if (typing == myId) {
                        activityMessageBinding.lottieAnimation.visibility= View.VISIBLE
                        activityMessageBinding.lottieAnimation.playAnimation()
                    } else {
                        activityMessageBinding.lottieAnimation.cancelAnimation()
                        activityMessageBinding.lottieAnimation.visibility = View.GONE

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun typingStatus(typing: String) {

        val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(myId)
        val map = HashMap<String, Any>()
        map["typing"] = typing
        databaseReference.updateChildren(map)

    }

    private fun getToken(message: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(hisId!!)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val token = snapshot.child("token").value.toString()

                    val to = JSONObject()
                    val data = JSONObject()

                    data.put("hisId", myId)
                    data.put("hisImage", myImage)
                    data.put("title", myName)
                    data.put("message", message)
                    data.put("chatId", chatId)

                    to.put("to", token)
                    to.put("data", data)
                   // sendNotification(to)


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

//    private fun sendNotification(to: JSONObject) {
//
//        val request: JsonObjectRequest = object : JsonObjectRequest(
//            Method.POST,
//            AppConstants.NOTIFICATION_URL,
//            to,
//            Response.Listener { response: JSONObject ->
//
//                Log.d("TAG", "onResponse: $response")
//            },
//            Response.ErrorListener {
//
//                Log.d("TAG", "onError: $it")
//            }) {
//            override fun getHeaders(): MutableMap<String, String> {
//                val map: MutableMap<String, String> = HashMap()
//
//                map["Authorization"] = "key=" + AppConstants.SERVER_KEY
//                map["Content-type"] = "application/json"
//                return map
//            }
//
//            override fun getBodyContentType(): String {
//                return "application/json"
//            }
//        }
//
//        val requestQueue = Volley.newRequestQueue(this)
//        request.retryPolicy = DefaultRetryPolicy(
//            30000,
//            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        )
//
//        requestQueue.add(request)
//
//    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)


    }




}