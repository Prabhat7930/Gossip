package com.example.gossip.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gossip.utils.Constants
import com.google.android.material.snackbar.Snackbar
import io.realm.kotlin.mongodb.App

open class BaseActivity : AppCompatActivity() {

    val app : App = App.create(Constants.APP_ID)

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun toastMessage(activity : Activity, msg : String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    fun snackbarMessage(msg : String) {
        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show()
    }

    fun hideKeyboard(activity: Activity) {
        val view : View = activity.findViewById(android.R.id.content)
        if (view != null) {
            val imm : InputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)

        }
    }
}