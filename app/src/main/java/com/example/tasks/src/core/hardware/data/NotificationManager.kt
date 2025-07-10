package com.example.tasks.src.core.hardware.data

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.tasks.R
import com.example.tasks.src.core.hardware.common.Constants
import com.example.tasks.src.core.hardware.domain.NotificationRepository

class NotificationManagerImpl(
    private val context: Context,
) : NotificationRepository {

    companion object {
        private const val TAG = "NotificationManager"
    }

    override fun activeNotification(idTask: String, taskTitle: String) {
        if (!hasNotificationPermission()) {
            Log.w(TAG, "No se pueden mostrar notificaciones: permisos faltantes")
            return
        }

        try {
            createNotificationChannelIfNeeded()

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            val notification = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Nueva tarea agregada")
                .setContentText("Tarea: $taskTitle. No olvides completarla antes de la fecha límite.")
                .setSmallIcon(R.drawable.outline_notification_add_24)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(longArrayOf(0, 250, 250, 250))
                .build()

            notificationManager.notify(idTask.hashCode(), notification)
            Log.d(TAG, "Notificación creada exitosamente")

        } catch (e: SecurityException) {
            Log.e(TAG, "Error de seguridad al crear notificación: ${e.message}")
        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado al crear notificación: ${e.message}")
        }
    }

    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        }
    }

    private fun createNotificationChannelIfNeeded() {
        val notificationManager = context.getSystemService(NotificationManager::class.java)

        // Verificar si el canal ya existe para evitar recrearlo
        if (notificationManager.getNotificationChannel(Constants.NOTIFICATION_CHANNEL_ID) == null) {
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                "Tareas",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal para notificaciones de tareas"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 250, 250, 250)

                val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
                setSound(soundUri, audioAttributes)
            }

            notificationManager.createNotificationChannel(channel)
            Log.d(TAG, "Canal de notificación creado")
        }
    }
}