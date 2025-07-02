package com.example.tasks.src.core.di

import android.content.Context
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.example.tasks.src.core.hardware.data.CamaraFactory
import com.example.tasks.src.core.hardware.domain.CamaraRepository

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
}