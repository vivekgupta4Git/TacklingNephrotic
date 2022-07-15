package com.ruviapps.tacklingnephrotic

import android.opengl.Visibility
import android.os.Bundle
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.ruviapps.tacklingnephrotic.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


//for dagger hilt , Activity class also need to annotate with entry point with fragment
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val passedIntent = intent
        val userName = passedIntent.getStringExtra(LoginActivity.INTENT_EXTRA_USERNAME)
        val phoneNumber = passedIntent.getStringExtra(LoginActivity.INTENT_EXTRA_PHONE)
        val isNewUser = passedIntent.getStringExtra(LoginActivity.INTENT_EXTRA_IS_NEW_USER).toBoolean()
        val email = passedIntent.getStringExtra(LoginActivity.INTENT_EXTRA_EMAIL)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

      setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        val graph = navController.navInflater.inflate(R.navigation.mobile_navigation)

       if(isNewUser) {
           graph.setStartDestination(R.id.nav_userRole)
       }else
           graph.setStartDestination(R.id.nav_result)
        navController.graph = graph

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_welcome,R.id.nav_userRole,
            R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_result), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    fun setBottomBarVisibility(visibility: Int){
        binding.appBarMain.toolbar.visibility = visibility
    }

    fun setFabVisibility(visibility: Int){
        binding.appBarMain.fab.visibility = visibility
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}