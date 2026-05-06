package com.adrianmalmierca.dijonevents

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.adrianmalmierca.dijonevents.data.api.DijonEventsApi
import com.adrianmalmierca.dijonevents.data.model.FcmTokenRequest
import com.adrianmalmierca.dijonevents.data.repository.TokenManager

@AndroidEntryPoint
class DijonFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var api: DijonEventsApi

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification?.title ?: "Dijon Événements"
        val body = remoteMessage.notification?.body ?: ""
        sendNotification(title, body)
    }

    //we generate a unique token for the device, we use it in the backend, to determinate to which device we sent the notification
    override fun onNewToken(fcmToken: String) {
        super.onNewToken(fcmToken) //call to the implementation of FirebaseMessagingService, just for future compatibility with Firebase
        //is not compulsory
        CoroutineScope(Dispatchers.IO).launch { //IO cause we do a http request
            try {
                val jwtToken = tokenManager.token.first() ?: return@launch
                api.updateFcmToken(
                    token = "Bearer $jwtToken",
                    request = FcmTokenRequest(fcmToken)
                )
            } catch (e: Exception) {
                println("Error updating FCM token: ${e.message}")
            }
        }
    }

    private fun sendNotification(title: String, body: String) {
        val channelId = "dijon_events_channel"
        val intent = Intent(this, MainActivity::class.java).apply { //what open when the user touch the notification
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) //if its already open, it reuses it, to avoid creating multiple instances
        }
        val pendingIntent = PendingIntent.getActivity( //permission so Android can execute the intent in the future
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT /*uses only once*/ or PendingIntent.FLAG_IMMUTABLE /*for security*/
        )

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Événements Dijon",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notifications pour les événements dijonnais"
        }
        notificationManager.createNotificationChannel(channel) //register the channel into the system

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true) //delete when we touch
            .setContentIntent(pendingIntent) //action to do when we touch (opens MainActivity)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification) //show the notification
        //unique id with timestamp to dont overwrite other notifications
    }
}