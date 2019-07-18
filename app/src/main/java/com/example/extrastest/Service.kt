package com.example.extrastest

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class Service : Service() {

    override fun onBind(intent: Intent): IBinder = Binder()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        "Service"
            .also {
                print(it, intent)
                send(it, this, intent)
            }
        return START_NOT_STICKY
    }
}
