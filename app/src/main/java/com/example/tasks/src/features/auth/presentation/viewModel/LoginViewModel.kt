package com.example.tasks.src.features.auth.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.src.features.auth.domain.models.LoginAuth
import com.example.tasks.src.features.auth.domain.usecase.LoginAuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class  LoginViewModel(
    private val accessUserUseCase: LoginAuthUseCase
): ViewModel(){

    private val _userId = MutableStateFlow<String>("")
    val userId: StateFlow<String> = _userId

    private var _email = MutableStateFlow<String>("")
    val email : StateFlow<String> = _email

    private var _password = MutableStateFlow<String>("")
    val password : StateFlow<String> = _password

    private val _message = MutableStateFlow<String>("")
    val message: StateFlow<String> = _message

    private val _success =MutableStateFlow<Boolean>(false)
    val success: StateFlow<Boolean> = _success

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading



    fun onChangeEmail(email: String){
        _email.value = email
    }

    fun onChangePassword (password: String) {
        _password.value = password
    }


    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    private  fun isPasswordValid(password: String): Boolean {
        return password.length >= 4
    }

    fun validateFields(): Boolean {
        val currentEmail = _email.value
        val currentPassword = _password.value

        return when {
            currentEmail.isBlank() -> {
                _message.value = "Email cannot be empty"
                _success.value = false
                false
            }
            currentPassword.isBlank() -> {
                _message.value = "Password cannot be empty"
                _success.value = false
                false
            }
            !isEmailValid(currentEmail) -> {
                _message.value = "Please enter a valid email address"
                _success.value = false
                false
            }
            !isPasswordValid(currentPassword) -> {
                _message.value = "The password must be at least 4 characters long"
                _success.value = false
                false
            }
            else -> {
                _message.value = ""
                true
            }
        }
    }

    fun resetFields() {
        _email.value = ""
        _password.value = ""
        _message.value = ""
        _success.value = false
    }


    fun onClick() {
        if (_isLoading.value) return

        _isLoading.value = true

        if (!validateFields()) {
            _isLoading.value = false
            return
        }

        val user = LoginAuth( correo = _email.value, contraseña = _password.value)

        viewModelScope.launch {
            try {
                val result = accessUserUseCase(user).getOrThrow()
                _userId.value = result.data.id
                _message.value = "Login successful"
                _success.value = true


            } catch (e: Exception) {
                
                _message.value = e.message ?: "Unknown error"
                _success.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }



}