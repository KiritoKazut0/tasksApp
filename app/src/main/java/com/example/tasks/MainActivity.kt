package com.example.tasks

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tasks.src.core.navegation.NavigationWrapper
import com.example.tasks.src.features.auth.di.AppModule

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "Todo Ok")
        AppModule.init(applicationContext)
        enableEdgeToEdge()
        setContent {
           NavigationWrapper()
        }
    }
}


