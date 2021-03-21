package com.example.schedule

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.schedule.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
//    private lateinit var drawerLayout: DrawerLayout
//    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }


        val binding= DataBindingUtil.setContentView<ActivityMainBinding>(this,
            R.layout.activity_main)

//        drawerLayout=binding.drawerLayout
//
//
//        val navController = this.findNavController(R.id.myNavHostFragment)
//        NavigationUI.setupActionBarWithNavController(this, navController,drawerLayout)
//        NavigationUI.setupWithNavController(binding.navView, navController)
        GlobalScope.launch {
            db = AppDatabase.getDatabase(applicationContext)
            val user = db.userDao().getAll()
            if(user.isNotEmpty() && user.first().askOnStart == true && user.first().hasLoggedIn == false){
                Log.w("MainPage", "Got to this bit")
                findNavController(R.id.myNavHostFragment).navigate(R.id.login2)
            }else if (user.isEmpty()){
                findNavController(R.id.myNavHostFragment).navigate(R.id.register2)
            } else {
                Log.w("MainPage", "So we got to the else")
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        GlobalScope.launch {
            db = AppDatabase.getDatabase(applicationContext)
            val userDAO = db.userDao()
            val user = userDAO.getAll().firstOrNull()
            user?.let {
                user.hasLoggedIn = false
                userDAO.updateUser(user)
            }
        }
    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = this.findNavController(R.id.myNavHostFragment)
//
//        return NavigationUI.navigateUp( navController,drawerLayout)    }


}
