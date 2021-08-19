package com.ruchanokal.mathmaster.classes

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ruchanokal.mathmaster.R
import com.ruchanokal.mathmaster.activities.MainActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {


        val intent = Intent(p0,MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(p0,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder = p0?.let { NotificationCompat.Builder(it,"notificationChannel") }
            ?.setSmallIcon(R.drawable.ic_baseline_notifications_24)
            ?.setContentTitle(p0.applicationContext.getString(R.string.beyin_sporu))
            ?.setContentText(p0.applicationContext.getString(R.string.beyin_sporu_aciklama))
            ?.setPriority(NotificationCompat.PRIORITY_DEFAULT)
            ?.setContentIntent(pendingIntent)
            ?.setAutoCancel(true)

        val notificationManagerCompat = p0?.let { NotificationManagerCompat.from(it) }

        if (notificationBuilder != null) {
            if (notificationManagerCompat != null) {
                notificationManagerCompat.notify(100,notificationBuilder.build())
            }
        }

    }
}