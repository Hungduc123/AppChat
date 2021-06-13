package com.phd.chat14android.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class User {
    var uid: String? = null
    var name: String? = null
    var profileImageUrl: String? = null
    constructor() {}
    constructor(uid:String?,name:String?,profileImageUrl:String?){
        this.uid = uid
        this.name = name
        this.profileImageUrl = profileImageUrl

    }
}


