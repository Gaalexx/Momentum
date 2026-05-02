package com.project.momentum

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyFirebaseMessagingService @Inject constructor() : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "FCM token refreshed")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "FCM message received from ${message.from}")
    }

    private companion object {
        const val TAG = "MomentumFcm"
    }
}
