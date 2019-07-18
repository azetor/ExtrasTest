package com.example.extrastest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager

const val EMPTY = "EMPTY"
const val ACTION_RUN = "ACTION_RUN"
const val EXTRA_NAVIGATION = "EXTRA_NAVIGATION"
const val CHANNEL_ID = "1234"
const val NOTIFICATION_ID = 1234

fun print(owner: String, intent: Intent?) {

    Log.d(owner, "Intent is null: ${intent == null}")
    Log.d(owner, "Action: ${(intent?.action) ?: EMPTY}")
    Log.d(owner, "Extras is empty: ${(intent?.extras?.isEmpty) ?: false}")
    (intent?.extras?.keySet() ?: emptySet())
        .forEach {
            Log.d(owner, "Key: $it, value: ${(intent?.extras?.get(it)) ?: EMPTY}")
        }
}

fun start(owner: String, context: Context, navigation: String) {

    context
        .startService(
            Intent(context, Service::class.java)
                .apply {
                    action = ACTION_RUN
                    putExtra(EXTRA_NAVIGATION, navigation)
                }
                .also {
                    Log.d(owner, "Service intent: $it")
                }
        )
}

fun notification(owner: String, context: Context, navigation: String) {

    createNotificationChannel(context)
    val pendingIntent = PendingIntent.getBroadcast(
        context, 0, Intent(context, Receiver::class.java)
            .apply {
                action = ACTION_RUN
                putExtra(EXTRA_NAVIGATION, navigation)
            }
            .also {
                Log.d(owner, "Notification intent: $it")
            }, 0
    )
        .also {
            Log.d(owner, "Notification pending intent: $it")
        }
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(android.R.drawable.sym_def_app_icon)
        .setContentTitle("EXTRAS_TEST_TITLE")
        .setContentText("EXTRAS_TEST_CONTENT")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .addAction(android.R.drawable.sym_def_app_icon, "TEST", pendingIntent)
        .setAutoCancel(true)

    with(NotificationManagerCompat.from(context)) {
        // notificationId is a unique int for each notification that you must define
        notify(NOTIFICATION_ID, builder.build())
        Log.d(owner, "Notification show: $builder")
    }
}

private fun createNotificationChannel(context: Context) {
    // Create the NotificationChannel,
    // but only on API 26+ because the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
            .createNotificationChannel(
                NotificationChannel(CHANNEL_ID, "EXTRAS_TEST", NotificationManager.IMPORTANCE_DEFAULT)
                    .apply {
                        description = "EXTRAS_TEST_DESCRIPTION"
                    }
            )
    }
}

fun send(owner: String, context: Context, intent: Intent?) {

    LocalBroadcastManager
        .getInstance(context)
        .sendBroadcast(
            Intent()
                .apply {
                    action = ACTION_RUN
                    putExtras(intent?.extras ?: Bundle()
                        .apply {
                            putString(
                                EMPTY,
                                EMPTY
                            )
                        }
                    ) // XXX other version with intent as extras
                }
                .also {
                    Log.d(owner, "Broadcast intent: $it")
                }
        )
}