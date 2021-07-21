package com.example.capstonesportapp

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.capstonesportapp.databinding.ActivityMainBinding
import com.scottyab.rootbeer.RootBeer
import kotlinx.android.synthetic.main.main_fragment.*
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        if (RootBeer(this).isRooted) {
            try {
                showRootedMessage()
            } catch (exception: Exception) {
                println(exception)
            }
        } else {
            with(binding) {
                setContentView(root)
                setSupportActionBar(incMainFragment.toolbar)

                val navHostFragment = supportFragmentManager
                    .findFragmentById(R.id.navHostFragment) as NavHostFragment
                navController = navHostFragment.navController

                toolbar.setTitleTextColor(resources.getColor(R.color.design_default_color_background))

                NavigationUI.setupWithNavController(navigationView, navController)
                setupActionBarWithNavController(navController, drawerLayout)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            navController,
            binding.drawerLayout
        )
    }

    private fun showRootedMessage() {
        val myApplication = MyApplication()
        Log.d(Log.WARN.toString(),"status: $myApplication")
        AlertDialog.Builder(this)
            .setTitle("DEVICE UNSAFE")
            .setMessage("App ini tidak dapat berjalan dalam mode rooted device")
            .setPositiveButton("Ok") { _, _ ->
                exitProcess(0)
            }
            .show()
    }
}