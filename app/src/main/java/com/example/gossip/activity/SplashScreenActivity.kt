package com.example.gossip.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.core.content.res.ResourcesCompat
import com.example.gossip.R
import com.example.gossip.databinding.ActivitySplashscreenBinding


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : BaseActivity() {

    private lateinit var binding : ActivitySplashscreenBinding
    private var username : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("username") != null) {
            username = intent.getStringExtra("username")
        }


        binding.tvAppNameG.typeface = ResourcesCompat.getFont(this, R.font.app_name_font)
        binding.tvAppNameO.typeface = ResourcesCompat.getFont(this, R.font.app_name_font)
        binding.tvAppNameS.typeface = ResourcesCompat.getFont(this, R.font.app_name_font)
        binding.tvAppNameS2.typeface = ResourcesCompat.getFont(this, R.font.app_name_font)
        binding.tvAppNameI.typeface = ResourcesCompat.getFont(this, R.font.app_name_font)
        binding.tvAppNameP.typeface = ResourcesCompat.getFont(this, R.font.app_name_font)

        val topAnimG = AnimationUtils.loadAnimation(this, R.anim.animation_splash_g)
        val topAnimO = AnimationUtils.loadAnimation(this, R.anim.animation_splash_o)
        val topAnimS = AnimationUtils.loadAnimation(this, R.anim.animation_splash_s)
        val topAnimS2 = AnimationUtils.loadAnimation(this, R.anim.animation_splash_s2)
        val topAnimI = AnimationUtils.loadAnimation(this, R.anim.animation_splash_i)
        val topAnimP = AnimationUtils.loadAnimation(this, R.anim.animation_splash_p)
        val bottomAnim = AnimationUtils.loadAnimation(this, R.anim.animation_splash_bottom)
        val gossipAnim = AnimationUtils.loadAnimation(this, R.anim.animation_splash_gossip)

        binding.tvAppNameP.startAnimation(topAnimP)
        binding.tvAppNameI.startAnimation(topAnimI)
        binding.tvAppNameS2.startAnimation(topAnimS2)
        binding.tvAppNameS.startAnimation(topAnimS)
        binding.tvAppNameO.startAnimation(topAnimO)
        binding.tvAppNameG.startAnimation(topAnimG)
        binding.tvAppDescriptionSplash.startAnimation(bottomAnim)
        binding.ivGossipSplash.startAnimation(gossipAnim)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashScreenActivity, AuthorizationActivity::class.java)
            intent.putExtra("username_logged", username)
            startActivity(intent)
            finish()
        }, 2000)
    }
}