package com.capstonech2.fitfans.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.databinding.ActivityMainBinding
import com.capstonech2.fitfans.ui.camera.CameraActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        navigationConfig()
        binding.buttonCamera.setOnClickListener { intentCamera() }
    }

    private fun navigationConfig(){
        val navView: BottomNavigationView = binding.bottomNavigation
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        binding.bottomNavigation.itemActiveIndicatorColor = getColorStateList(R.color.white)
    }

    private fun intentCamera(){
        startActivity(Intent(this, CameraActivity::class.java))
    }
}