package com.example.tasks.src.core.di

import android.content.Context
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.example.tasks.src.core.appcontext.AppContextHolder
import com.example.tasks.src.core.hardware.data.CamaraFactory
import com.example.tasks.src.core.hardware.data.NotificationManagerImpl
import com.example.tasks.src.core.hardware.data.VibratorRepositoryImpl
import com.example.tasks.src.core.hardware.domain.CamaraRepository
import com.example.tasks.src.core.hardware.domain.NotificationRepository
import com.example.tasks.src.core.hardware.domain.VibratorRepository
import kotlin.text.get

object HardwareModule {
   lateinit var cameraFactory : CamaraRepository

      private set

       fun initCameraFactory(
         contex: Context,
         lifecycleOwner: LifecycleOwner,
         previewView: PreviewView
      ){
         cameraFactory = CamaraFactory.create(contex, lifecycleOwner, previewView)
      }

    val notificationManager: NotificationRepository by lazy {
        NotificationManagerImpl(AppContextHolder.get())
    }

    val vibratorManager: VibratorRepository by lazy {
        VibratorRepositoryImpl(AppContextHolder.get())
    }

}