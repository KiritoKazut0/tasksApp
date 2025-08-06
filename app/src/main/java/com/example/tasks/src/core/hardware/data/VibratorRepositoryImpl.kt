package com.example.tasks.src.core.hardware.data

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import com.example.tasks.src.core.hardware.domain.VibratorRepository

class VibratorRepositoryImpl(
    private val context: Context
): VibratorRepository {

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        context.getSystemService(Vibrator::class.java)
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    override fun vibrate(milliseconds: Long) {
        try {
            vibrator?.let { vib ->
                if (vib.hasVibrator()) {
                    Log.d("VibratorRepo", "Intentando vibrar por $milliseconds ms")

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val effect = VibrationEffect.createOneShot(
                            milliseconds,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                        vib.vibrate(effect)
                    } else {
                        @Suppress("DEPRECATION")
                        vib.vibrate(milliseconds)
                    }

                    Log.d("VibratorRepo", "Vibración ejecutada correctamente")
                } else {
                    Log.w("VibratorRepo", "El dispositivo no tiene vibrador")
                }
            } ?: Log.e("VibratorRepo", "No se pudo obtener el servicio de vibrador")
        } catch (e: Exception) {
            Log.e("VibratorRepo", "Error al vibrar: ${e.message}", e)
        }
    }


}