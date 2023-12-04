package com.example.gossip.activity

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseRef(context : Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    //CREATE USER TABLE
    private val CREATE_TABLE_USER = ("CREATE TABLE $TABLE_USER (" +
            "$COL_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$COL_USER_NAME TEXT, " +
            "$COL_USER_EMAIL TEXT, " +
            "$COL_USER_PASSWORD TEXT)")

    //DROP TABLE WHEN SOMETHING CHANGE IN DB
    private val DROP_TABLE_USER = "DROP TABLE IF EXISTS $TABLE_USER"
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DROP_TABLE_USER)
    }

    fun registerUser(user: User){
        val db = this.writableDatabase
        val value = ContentValues()
        value.put(COL_USER_NAME, user.username)
        value.put(COL_USER_EMAIL, user.email)
        value.put(COL_USER_PASSWORD, user.password)

        db.insert(TABLE_USER, null, value)
        db.close()
    }
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Gossip.db"
        private const val TABLE_USER = "tbl_user"

        private const val COL_USER_ID = "user_id"
        private const val COL_USER_NAME = "user_name"
        private const val COL_USER_EMAIL = "user_email"
        private const val COL_USER_PASSWORD = "user_password"
    }


}