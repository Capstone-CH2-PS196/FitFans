package com.capstonech2.fitfans.ui.timer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.ui.history.HistoryActivity
import com.capstonech2.fitfans.utils.FITFANS_TITLE
import com.capstonech2.fitfans.utils.NOTIFICATION_CHANNEL_ID

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    private val fitfansTitle = inputData.getString(FITFANS_TITLE)

    override fun doWork(): Result {
        if (fitfansTitle != null){
            val intent = Intent(applicationContext, HistoryActivity::class.java)
            val pendingIntent = TaskStackBuilder.create(applicationContext).run {
                addNextIntentWithParentStack(intent)
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            }

            val notificationManagerCompat = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(fitfansTitle)
                .setContentText(applicationContext.getString(R.string.notify_content))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                builder.setChannelId(NOTIFICATION_CHANNEL_ID)
                notificationManagerCompat.createNotificationChannel(channel)
            }

            val notification = builder.build()
            notificationManagerCompat.notify(100, notification)
        }
        return Result.success()
    }

    companion object{
        private const val NOTIFICATION_CHANNEL_NAME = "fitfans_channel"
    }
}