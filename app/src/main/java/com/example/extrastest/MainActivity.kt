package com.example.extrastest

import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val textMessage: TextView by lazy { findViewById<TextView>(R.id.message) }
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                textMessage.setText(R.string.title_home)
                startService()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                textMessage.setText(R.string.title_dashboard)
                startService()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                textMessage.setText(R.string.title_notifications)
                notification()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun startService() {

        start("MainActivity", application, textMessage.text.toString())
    }

    private fun notification() {

        notification("MainActivity", application, textMessage.text.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        findViewById<BottomNavigationView>(R.id.nav_view).setOnNavigationItemSelectedListener(
            onNavigationItemSelectedListener
        )

        // App is build for SDK 23 but this is friendly reminder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(Receiver(), IntentFilter(ACTION_RUN))
        }
    }
}
