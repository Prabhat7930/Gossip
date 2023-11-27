package com.example.gossip.data

import android.util.Log
import com.example.gossip.model.User
import com.example.gossip.utils.Constants
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.mongodb.App

object MongoDB : MongoFunctions{
    private val app = App.Companion.create(Constants.APP_ID)
    private val user = app.currentUser
    private lateinit var realm : Realm

    init {
        configureTheRealm()
    }

    override fun configureTheRealm() {
        if (user != null) {
            val config = RealmConfiguration.create(schema = setOf(User::class))
            realm = Realm.open(config)
        }
    }

    override suspend fun addUserInfo(username : String, email : String) {
        if (user != null) {
            realm.write {
                try {
                    val Person = User().apply {
                        owner_id = user.id
                        userName = username
                        userEmail = email
                    }
                    copyToRealm(Person)
                }
                catch (e : Exception) {
                    Log.d("TAG", "${e.message}")
                }
            }
        }
    }
}