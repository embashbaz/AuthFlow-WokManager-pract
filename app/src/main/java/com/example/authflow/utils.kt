package com.example.authflow

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val KEY_IMAGE_URI = "KEY_IMAGE_URI"
@JvmField val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence =
    "Upload image in the background"
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
    "show upload status"
const val CHANNEL_ID = "UPLOAD_IMAGE_NOTIFICATION"
const val NOTIFICATION_ID = 1

fun <T> Fragment.collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}

fun <T> AppCompatActivity.collectLatestLifecycleFlowActivity(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}

fun makeStatusNotification(message: String, context: Context) {


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        val name = VERBOSE_NOTIFICATION_CHANNEL_NAME
        val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = description

        // Add the channel
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)
    }

    // Create the notification
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Image Upload")
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setOnlyAlertOnce(true)
        .setVibrate(LongArray(0))

    // Show the notification
    NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
}