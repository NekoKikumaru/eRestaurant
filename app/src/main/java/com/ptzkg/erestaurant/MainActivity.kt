package com.ptzkg.erestaurant

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = navigation_fragment as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.navigation)

        var sharedPrefs: SharedPreferences = this.getSharedPreferences("user", Context.MODE_PRIVATE)
        var user_id = sharedPrefs.getInt("user_id", -1)

        if (user_id == -1) {
            graph.startDestination = R.id.authFragment
        }
        else {
            graph.startDestination = R.id.homeFragment
        }
        navHostFragment.navController.graph = graph
    }
}