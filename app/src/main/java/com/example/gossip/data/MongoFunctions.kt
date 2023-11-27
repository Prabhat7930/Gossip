package com.example.gossip.data

interface MongoFunctions {
    fun configureTheRealm()
    suspend fun addUserInfo(username : String, email : String)

}