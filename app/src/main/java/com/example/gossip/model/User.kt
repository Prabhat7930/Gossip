package com.example.gossip.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class User : RealmObject {
    @PrimaryKey
    var _id : ObjectId = ObjectId.invoke()
    var owner_id : String = ""
    var userName : String = ""
    var userEmail : String = ""
}