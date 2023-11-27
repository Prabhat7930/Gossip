package com.example.gossip.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.gossip.R
import com.example.gossip.data.MongoDB
import com.example.gossip.databinding.ActivityAuthorizationBinding
import io.realm.kotlin.mongodb.Credentials
import kotlinx.coroutines.runBlocking


class AuthorizationActivity : BaseActivity() {

    private lateinit var binding : ActivityAuthorizationBinding

    companion object {
        const val POST_NOTIFICATION_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var isKeyboardVisible = false

        checkPermissions()

        binding.tvConnectAuth.typeface = ResourcesCompat.getFont(this, R.font.app_name_font)

        binding.tvSignInAuth.setOnClickListener {
            binding.tvSignInAuth.setTextColor(getColor(R.color.black))
            binding.viewSignInAuth.setBackgroundColor(getColor(R.color.black))
            binding.tvSignUpAuth.setTextColor(getColor(R.color.textColorLight))
            binding.viewSignUpAuth.setBackgroundColor(getColor(R.color.viewColorLight))

            binding.llSignInAuth.visibility = View.VISIBLE
            binding.llSignUpAuth.visibility = View.GONE

            hideKeyboard(this@AuthorizationActivity)
            binding.etUsernameSignUpAuth.clearFocus()
            binding.etEmailSignUpAuth.clearFocus()
            binding.etPasswordSignUpAuth.clearFocus()
            binding.etConfirmPasswordSignUpAuth.clearFocus()

            toastMessage(this@AuthorizationActivity, "hello")

        }

        binding.tvSignUpAuth.setOnClickListener {
            binding.tvSignUpAuth.setTextColor(getColor(R.color.black))
            binding.viewSignUpAuth.setBackgroundColor(getColor(R.color.black))
            binding.tvSignInAuth.setTextColor(getColor(R.color.textColorLight))
            binding.viewSignInAuth.setBackgroundColor(getColor(R.color.viewColorLight))

            binding.llSignUpAuth.visibility = View.VISIBLE
            binding.llSignInAuth.visibility = View.GONE

            hideKeyboard(this@AuthorizationActivity)
            binding.etEmailSignInAuth.clearFocus()
            binding.etPasswordSignInAuth.clearFocus()

        }

        /*binding.etMobileNumberRegister.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //no need to implement
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrBlank()) {
                    binding.ivCancelNumberInputRegister.visibility = View.VISIBLE
                } else {
                    binding.ivCancelNumberInputRegister.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                //no need to implement
            }

        })*/

        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val visibleRect = Rect()
            binding.root.getWindowVisibleDisplayFrame(visibleRect)
            val screenHeight = binding.root.rootView.height
            val keyPadHeight = screenHeight - visibleRect.bottom

            Log.d("TAG", "keypadHeight = $keyPadHeight")

            if (keyPadHeight > screenHeight * 0.15) {
                if (!isKeyboardVisible) {
                    isKeyboardVisible = true
                    onKeyboardVisibility(true)
                }
            } else {
                if (isKeyboardVisible) {
                    isKeyboardVisible = false
                    onKeyboardVisibility(false)
                }
            }
        }

        binding.btnSignInAuth.setOnClickListener {
            signIn()
        }

        binding.btnSignUpAuth.setOnClickListener {
            signUp()
        }

    }

    private fun signIn() {
        if (binding.etEmailSignInAuth.text.isNullOrBlank()) {
            toastMessage(this@AuthorizationActivity, "Please enter all the fields")
        }
        else if (binding.etPasswordSignInAuth.text.isNullOrBlank()) {
            toastMessage(this@AuthorizationActivity, "Please enter all the fields")
        }
        else if (binding.etPasswordSignInAuth.text!!.length < 6) {
            toastMessage(this@AuthorizationActivity, "Password must be at least 6 characters long")
        }
        else {
            val username = binding.etUsernameSignUpAuth.text.toString()
            val email = binding.etEmailSignInAuth.text.toString()
            val password = binding.etPasswordSignInAuth.text.toString()
            runBlocking {
                val emailPasswordCred = Credentials.emailPassword(email, password)
                try {
                    val user = app.login(emailPasswordCred)
                    if (user != null) {
                        MongoDB.configureTheRealm()
                        MongoDB.addUserInfo(username, email)
                        toastMessage(this@AuthorizationActivity, "Signing in")
                        startActivity(Intent(this@AuthorizationActivity, MainActivity::class.java))
                        finish()
                    }
                }
                catch (e : Exception) {
                    toastMessage(this@AuthorizationActivity, "Wrong Credentials")
                }

            }

        }
    }

    private fun signUp() {
        if (binding.etUsernameSignUpAuth.text.isNullOrBlank()) {
            toastMessage(this@AuthorizationActivity, "Please enter all the fields")
        }
        else if (binding.etEmailSignUpAuth.text.isNullOrBlank()) {
            toastMessage(this@AuthorizationActivity, "Please enter all the fields")
        }
        else if (binding.etPasswordSignUpAuth.text.isNullOrBlank()) {
            toastMessage(this@AuthorizationActivity, "Please enter all the fields")
        }
        else if (binding.etConfirmPasswordSignUpAuth.text.isNullOrBlank()) {
            toastMessage(this@AuthorizationActivity, "Please enter all the fields")
        }

        if (binding.etPasswordSignUpAuth.text!!.length < 6) {
            toastMessage(this@AuthorizationActivity, "Password must be at least 6 characters long")
        }
        else {
            if (binding.etPasswordSignUpAuth.text.toString() != binding.etConfirmPasswordSignUpAuth.text.toString()) {
                toastMessage(this@AuthorizationActivity, "Password does not match")
            }
            else {
                val email = binding.etEmailSignUpAuth.text.toString()
                val password = binding.etPasswordSignUpAuth.text.toString()
                runBlocking {
                    app.emailPasswordAuth.registerUser(email, password)
                    toastMessage(this@AuthorizationActivity, "Signing up")
                    startActivity(Intent(this@AuthorizationActivity, SplashScreenActivity::class.java))
                    finish()
                }

            }

        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED) {
            Log.d("TAG", "Permission Granted")
        }
        else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                POST_NOTIFICATION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == POST_NOTIFICATION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("TAG", "Permission Granted")
            }
            else {
                openPermissionAlertDialog()
            }
        }
    }

    private fun onKeyboardVisibility(isVisible : Boolean) {
        if (!isVisible) {
            binding.etEmailSignInAuth.clearFocus()
            binding.etPasswordSignInAuth.clearFocus()

            binding.etUsernameSignUpAuth.clearFocus()
            binding.etEmailSignUpAuth.clearFocus()
            binding.etPasswordSignUpAuth.clearFocus()
            binding.etConfirmPasswordSignUpAuth.clearFocus()
        }
    }

    private fun openPermissionAlertDialog() {

        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )

        val alertDialog : AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setMessage("This App requires to send message")
        alertDialog.setPositiveButton("Try Again")
        {_, _ -> checkPermissions() }
        alertDialog.setNegativeButton("Settings")
        {_, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            val packageName = "com.example.gossip"
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
                finish()
            }

        }

        val alert : AlertDialog = alertDialog.create()
        alert.setCancelable(false)
        alert.show()
    }
}