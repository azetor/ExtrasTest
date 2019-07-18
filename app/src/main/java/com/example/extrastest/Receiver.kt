package com.example.extrastest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class Receiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        "Receiver"
            .also {
                print(it, intent)
            }
    }
}
