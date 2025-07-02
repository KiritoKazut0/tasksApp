package com.example.tasks.src.core.hardware.data

import android.content.Context
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.example.tasks.src.core.hardware.domain.CamaraRepository

object CamaraFactory {
    fun create(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView
    ): CamaraRepository {
        return CamaraManager(context, lifecycleOwner, previewView)
    }
}
