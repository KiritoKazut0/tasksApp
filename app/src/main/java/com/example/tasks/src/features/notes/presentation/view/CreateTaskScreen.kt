package com.example.tasks.src.features.notes.presentation.view

import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasks.R
import com.example.tasks.src.core.di.HardwareModule
import com.example.tasks.src.features.notes.di.AppModule
import com.example.tasks.src.features.notes.domain.models.TaskStatus
import com.example.tasks.src.features.notes.presentation.view.comon.FullScreenError
import com.example.tasks.src.features.notes.presentation.view.comon.FullScreenLoader
import com.example.tasks.src.features.notes.presentation.viewModel.CameraViewModel
import com.example.tasks.src.features.notes.presentation.viewModel.CreateTaskViewModel
import com.example.tasks.src.features.notes.presentation.viewModel.factory.CameraViewModelFactory
import com.example.tasks.src.features.notes.presentation.viewModel.factory.CreateTaskViewModelFactory
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CreateTaskScreen(
    userId: String,
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }

    var cameraInitialized by remember { mutableStateOf(false) }
    var cameraError by remember { mutableStateOf<String?>(null) }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            HardwareModule.initCameraFactory(context, lifecycleOwner, previewView)
            cameraInitialized = true
            delay(300)
            isVisible = true
        } catch (e: Exception) {
            cameraError = "Error al inicializar cámara: ${e.localizedMessage}"
        }
    }

    when {
        cameraError != null -> {
            FullScreenError(message = cameraError!!) {
                cameraError = null
                cameraInitialized = false
            }
            return
        }

        !cameraInitialized -> {
            FullScreenLoader()
            return
        }
    }

    // 2. ViewModels
    val cameraViewModel: CameraViewModel = viewModel(
        factory = CameraViewModelFactory(HardwareModule.cameraFactory)
    )

    val viewModelTask: CreateTaskViewModel = viewModel(
        factory = CreateTaskViewModelFactory(
            AppModule.createTaskUseCase,
            HardwareModule.notificationManager,
            AppModule.uploadFileUseCase

        )
    )

    // Estados de UI
    val title by viewModelTask.title.collectAsState()
    val description by viewModelTask.description.collectAsState()
    val selectedStatus by viewModelTask.selectedStatus.collectAsState()
    val isLoading by viewModelTask.isLoading.collectAsState()
    val showSuccess by viewModelTask.showSuccess.collectAsState()
    val errorMessage by viewModelTask.errorMessage.collectAsState()

    val options = TaskStatus.entries

    val statusColors = mapOf(
        TaskStatus.CANCELADA to Color(0xFFE57373),
        TaskStatus.EN_PROGRESO to Color(0xFF64B5F6),
        TaskStatus.COMPLETADA to Color(0xFF81C784),
        TaskStatus.PENDIENTE to Color(0xFFBDBDBD)
    )

    // UI Principal
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(0.3f),
                        painter = painterResource(R.drawable.font_app),
                        contentDescription = "Sticky Notes Logo",
                        contentScale = ContentScale.Crop
                    )

                    Box(modifier = Modifier.fillMaxSize())

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(24.dp)
                    ) {
                        Text(
                            text = "Create",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Light,
                            color = Color(0xFF333333)
                        )
                        Text(
                            text = "Tasks",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                    }
                }
            }

            // Contenido principal con animación
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = spring()
                ) + fadeIn()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Sección de título
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "Task title",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color(0xFF666666),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            OutlinedTextField(
                                value = title,
                                onValueChange = viewModelTask::onTitleChanged,
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(
                                        "Enter the title...",
                                        color = Color(0xFFAAAAAA)
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF666666),
                                    unfocusedBorderColor = Color(0xFFCCCCCC),
                                    focusedTextColor = Color(0xFF333333),
                                    unfocusedTextColor = Color(0xFF333333)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                        }
                    }

                    // Sección de estado
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "Task status",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color(0xFF666666),
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(options.size) { index ->
                                    val option = options[index]
                                    val isSelected = selectedStatus == option

                                    FilterChip(
                                        onClick = { viewModelTask.onStatusSelected(option) },
                                        label = {
                                            Text(
                                                option.name,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) Color.White else Color(0xFF666666)
                                            )
                                        },
                                        selected = isSelected,
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = statusColors[option] ?: Color(0xFF666666),
                                            selectedLabelColor = Color.White,
                                            containerColor = Color(0xFFF5F5F5),
                                            labelColor = Color(0xFF666666)
                                        ),
                                        modifier = Modifier.scale(
                                            animateFloatAsState(
                                                targetValue = if (isSelected) 1.1f else 1f,
                                                animationSpec = spring(),
                                                label = "chip_scale"
                                            ).value
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Sección de cámara
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "Photos",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color(0xFF666666),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            CameraScreenComponent(
                                cameraViewModel = cameraViewModel,
                                previewView = previewView
                            )
                        }
                    }

                    // Sección de descripción
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "Description",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color(0xFF666666),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            OutlinedTextField(
                                value = description,
                                onValueChange = viewModelTask::onDescriptionChanged,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                placeholder = {
                                    Text(
                                        "Describe your task...",
                                        color = Color(0xFFAAAAAA)
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF666666),
                                    unfocusedBorderColor = Color(0xFFCCCCCC),
                                    focusedTextColor = Color(0xFF333333),
                                    unfocusedTextColor = Color(0xFF333333)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                maxLines = 4
                            )
                        }
                    }

                    // Botón de guardar
                    Button(
                        onClick = {
                            val images = cameraViewModel.getPhoto()
                            viewModelTask.createTask(userId, images)
                        },
                        enabled = !isLoading && title.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD9FF1D)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.Black
                            )
                        } else {
                            Text(
                                text = "Save Task",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }

                    // Mostrar errores
                    errorMessage?.let { message ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFEBEE)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Text(
                                text = message,
                                color = Color(0xFFD32F2F),
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        // Mensaje de éxito animado
        AnimatedVisibility(
            visible = showSuccess,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF666666)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(
                                Color.White,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✓",
                            color = Color(0xFF666666),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "¡Tarea creada exitosamente!",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }

    // Manejo del mensaje de éxito
    LaunchedEffect(showSuccess) {
        if (showSuccess) {
            delay(3000)
            viewModelTask.clearSuccess()
            onNavigateBack()
        }
    }
}

