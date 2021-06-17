package com.phd.chat14android.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.phd.chat14android.R
import com.phd.chat14android.adapter.listFriendAdapter
import com.phd.chat14android.models.User
import java.lang.StringBuilder


@Suppress("UNREACHABLE_CODE", "UnusedEquals", "IMPLICIT_CAST_TO_ANY")
class ChatFragment : Fragment() {



    private var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("/users")
    lateinit var recycleView: RecyclerView
    var layoutManager: GridLayoutManager? = null
    var users= ArrayList<User>();
    var currentUser = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v : View = inflater.inflate(R.layout.fragment_chat, container, false)
       recycleView = v.findViewById(R.id.rvListFriend)
        val adapter=listFriendAdapter()
        recycleView.adapter=adapter
        adapter.dataSet=users
//      layoutManager = GridLayoutManager(v.context, 1)
//       recycleView.layoutManager = layoutManager
//       recycleView.adapter = listFriendAdapter(users)
//        val headerAdapter = HeaderAdapter()
//        val flowersAdapter = FlowersAdapter { flower -> adapterOnClick(flower) }
//        val concatAdapter = ConcatAdapter(headerAdapter, flowersAdapter)
//        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
//        recycleView.adapter = concatAdapter
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var uidCr = Firebase.auth.currentUser
        var getData = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                var sb = StringBuilder()



                for (i in p0.children) {

                    if (uidCr != null) {
                        if (uidCr.uid == i.child("uid").value) {
                            currentUser.name = i.child("name").value as String

                            currentUser.profileImageUrl = i.child("profileImageUrl").value as String
                            currentUser.uid = i.child("uid").value as String
                            currentUser.status = i.child("status").value as String
                            currentUser.online = i.child("online").value as String

                        } else {


                            users.add(
                                User(
                                    i.child("uid").value as String?,
                                    i.child("name").value as String?,
                                    i.child("profileImageUrl").value as String?,
                                    i.child("status").value as String,
                                    i.child("online").value as String
                                )
                            )

                        }
                    }
                }
                Log.i("AAAAA",currentUser.toString())
                Log.i("AAAAA",users.toString())



            }

            override fun onCancelled(p0: DatabaseError) {
            }

        }
        database.addValueEventListener(getData)
        database.addListenerForSingleValueEvent(getData)
    }

}



