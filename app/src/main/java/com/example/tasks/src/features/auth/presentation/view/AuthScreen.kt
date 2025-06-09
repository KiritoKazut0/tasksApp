package com.example.tasks.src.features.auth.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier


@Composable
fun  AuthScreen (navigateToHome: (String) -> Unit){

    val isLogin = remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize()){
            if (isLogin.value){
                LoginScreen(
                    onToggle = {isLogin.value = false},
                    onLoginSuccess = { userId ->
                        navigateToHome(userId)
                    }
                )
            } else{
                RegisterScreen(
                    onToggle = {isLogin.value = true},
                    onLoginSuccess = { userId ->
                        navigateToHome(userId)
                    }
                )
            }
    }
}