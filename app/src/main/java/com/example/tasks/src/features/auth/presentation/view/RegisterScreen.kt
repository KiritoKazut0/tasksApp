package com.example.tasks.src.features.auth.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import  com.example.tasks.R
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasks.src.features.auth.di.AppModule
import com.example.tasks.src.features.auth.presentation.viewModel.RegisterViewModel
import com.example.tasks.src.features.auth.presentation.viewModel.RegisterViewModelFactory
import kotlinx.coroutines.launch


@Composable()
fun RegisterScreen (
    onToggle: () -> Unit,
    onLoginSuccess: (String)-> Unit,

    ) {

    val viewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModelFactory(AppModule.registerUseCase)
    )


    val name: String by viewModel.name.collectAsState("")
    val email: String by viewModel.email.collectAsState("")
    val password: String by viewModel.password.collectAsState("")
    val message: String by viewModel.message.collectAsState("")
    val success: Boolean by viewModel.success.collectAsState(false)
    val isLoading: Boolean by viewModel.isLoading.collectAsState(false)
    val userId: String by viewModel.userId.collectAsState()


    LaunchedEffect(success, userId) {
        if (success && userId.isNotEmpty()) {
            onLoginSuccess(userId)
        }
    }


    LaunchedEffect (Unit) {
        viewModel.resetFields()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFCCCCCC))
            .padding(vertical = 30.dp)
    ) {

        Image(
            painter = painterResource(id = R.drawable.font_app),
            contentDescription = "Sticky Notes Background",
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight(1f)
                .offset(y = (-380).dp),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Sticky Notes Logo",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 160.dp)
                .fillMaxWidth(0.38f)
                .zIndex(3f),
            contentScale = ContentScale.Fit

        )

        Column (
            modifier = Modifier
                .fillMaxHeight(0.62f)
                .fillMaxWidth(0.8f)
                .align(Alignment.Center)
                .offset(y = 85.dp)
                .background(Color.White, shape = RoundedCornerShape(70.dp))
                .border(20.dp, Color.White, RoundedCornerShape(20.dp))

                .padding(start = 30.dp, end = 30.dp, top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LoginSignUpToggle(
                selected = false,
                onToggle = { onToggle()}
            )

            Text(
                modifier = Modifier.padding(vertical = 5.dp),
                text = "Welcome to NoteApp",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.W400
            )


            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                placeholder = { Text(
                    text = "Name",
                    fontSize = 15.sp,
                    color = Color.Gray
                ) },
                onValueChange = {viewModel.onChangeUsername(it)},
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = Color.Gray,
                    focusedIndicatorColor = Color.Gray,
                    unfocusedIndicatorColor = Color.Gray
                )

            )


            TextField(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 12.dp)
                ,
                value = email,
                placeholder = {  Text(
                    text = "E-mail",
                    fontSize = 15.sp,
                    color = Color.Gray
                ) },
                onValueChange = {viewModel.onChangeEmail(it)},
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = Color.Gray,
                    focusedIndicatorColor = Color.Gray,
                    unfocusedIndicatorColor = Color.Gray
                )

            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                placeholder = { Text(
                    text = "Password",
                    fontSize = 15.sp,
                    color = Color.Gray
                ) },
                onValueChange = {viewModel.onChangePassword(it)},
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = Color.Gray,
                    focusedIndicatorColor = Color.Gray,
                    unfocusedIndicatorColor = Color.Gray
                )

            )


            Button(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 28.dp)
                    .fillMaxHeight(0.45f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD9FF1D)
                ),
                enabled = !isLoading,
                onClick = {
                    viewModel.viewModelScope.launch {
                        viewModel.onClick()
                    }
                 }
            ) {

                Text(
                    color = Color.Black,
                    text = "Sigup"
                )
            }


            if (message.isNotEmpty()) {
                Text(
                    text = message,
                    color = if (success) Color(0xFF4CAF50) else Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 14.dp)
                )
            }




        }


    }

}







