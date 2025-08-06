package com.example.tasks.src.features.notes.presentation.view

import androidx.camera.view.PreviewView
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
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
import androidx.compose.ui.platform.LocalContext

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
    var startAnimations by remember { mutableStateOf(false) }

    // Animaciones de entrada
    val headerAlpha by animateFloatAsState(
        targetValue = if (startAnimations) 1f else 0f,
        animationSpec = tween(800, delayMillis = 200),
        label = "headerAlpha"
    )

    val contentScale by animateFloatAsState(
        targetValue = if (startAnimations) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = 0.7f,
            stiffness = 300f
        ),
        label = "contentScale"
    )

    LaunchedEffect(Unit) {
        try {
            HardwareModule.initCameraFactory(context, lifecycleOwner, previewView)
            cameraInitialized = true
            delay(300)
            startAnimations = true
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

    // ViewModels
    val cameraViewModel: CameraViewModel = viewModel(
        factory = CameraViewModelFactory(HardwareModule.cameraFactory)
    )

    val viewModelTask: CreateTaskViewModel = viewModel(
        factory = CreateTaskViewModelFactory(
            AppModule.createTaskUseCase,
            HardwareModule.notificationManager,
            AppModule.uploadFileUseCase,
            HardwareModule.vibratorManager
        )
    )

    // Estados de UI
    val title by viewModelTask.title.collectAsState()
    val description by viewModelTask.description.collectAsState()
    val selectedStatus by viewModelTask.selectedStatus.collectAsState()
    val isLoading by viewModelTask.isLoading.collectAsState()
    val showSuccess by viewModelTask.showSuccess.collectAsState()
    val errorMessage by viewModelTask.errorMessage.collectAsState()
    val isVisible by remember { mutableStateOf(true) }

    val options = TaskStatus.entries

    val statusColors = mapOf(
        TaskStatus.CANCELADA to Color(0xFFE57373),
        TaskStatus.EN_PROGRESO to Color(0xFF64B5F6),
        TaskStatus.COMPLETADA to Color(0xFF81C784),
        TaskStatus.PENDIENTE to Color(0xFFBDBDBD)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF667eea),
                        Color(0xFF764ba2).copy(alpha = 0.3f),
                        Color.White
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header moderno con gradiente
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .alpha(headerAlpha)
            ) {
                // Elementos flotantes de fondo
                FloatingCreateElements()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Botón de regreso
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .background(
                                Color.White.copy(alpha = 0.2f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Título de la pantalla
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = Color.White,
                                shape = CircleShape,
                                shadowElevation = 8.dp,
                                modifier = Modifier.size(50.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Create,
                                        contentDescription = "Create Task",
                                        tint = Color(0xFF667eea),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = "Create",
                                    fontSize = 18.sp,
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontWeight = FontWeight.Light
                                )
                                Text(
                                    text = "New Task",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // Contenido principal con tarjetas modernas
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = spring()
                ) + fadeIn()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(contentScale)
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Tarjeta de título con diseño moderno
                    ModernCreateCard(isExpanded = false) {
                        ModernCreateTextField(
                            value = title,
                            onValueChange = { viewModelTask.onTitleChanged(it) },
                            label = "Task Title",
                            placeholder = "What do you need to do?"
                        )
                    }

                    // Tarjeta de estado con chips animados
                    ModernCreateCard(isExpanded = false) {
                        Column {
                            Text(
                                text = "Task Status",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color(0xFF374151),
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(options.size) { index ->
                                    val option = options[index]
                                    val isSelected = selectedStatus == option

                                    StatusCreateChip(
                                        status = option,
                                        isSelected = isSelected,
                                        color = statusColors[option] ?: Color.Gray,
                                        onClick = { viewModelTask.onStatusSelected(option) }
                                    )
                                }
                            }
                        }
                    }

                    // Tarjeta de cámara
                    ModernCreateCard(isExpanded = false) {
                        Column {
                            Text(
                                text = "Add Photos",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color(0xFF374151),
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            CameraScreenComponent(
                                cameraViewModel = cameraViewModel,
                                previewView = previewView
                            )
                        }
                    }

                    // Tarjeta de descripción
                    ModernCreateCard(isExpanded = false) {
                        ModernCreateTextField(
                            value = description,
                            onValueChange = { viewModelTask.onDescriptionChanged(it) },
                            label = "Description",
                            placeholder = "Describe your task in detail...",
                            maxLines = 4,
                            minLines = 3
                        )
                    }

                    // Botón de crear con estilo moderno
                    Button(
                        onClick = {
                            val images = cameraViewModel.getPhoto()
                            viewModelTask.createTask(userId, images)
                        },
                        enabled = !isLoading && title.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF667eea),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(20.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 8.dp,
                            pressedElevation = 4.dp
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = "Create",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Create Task",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Mostrar errores con estilo moderno
                    errorMessage?.let { message ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFF6B6B).copy(alpha = 0.1f)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Error,
                                    contentDescription = "Error",
                                    tint = Color(0xFFFF6B6B),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = message,
                                    color = Color(0xFFFF6B6B),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // Espaciado final
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }

        // Mensaje de éxito moderno
        AnimatedVisibility(
            visible = showSuccess,
            enter = scaleIn(
                animationSpec = spring(
                    dampingRatio = 0.7f,
                    stiffness = 300f
                )
            ) + fadeIn(),
            exit = scaleOut() + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF10B981)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color.White,
                        shape = CircleShape,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Success",
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "Task created successfully!",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
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

@Composable
fun ModernCreateCard(
    isExpanded: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isExpanded) 12.dp else 8.dp,
            hoveredElevation = 16.dp
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            content = content
        )
    }
}

@Composable
fun ModernCreateTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    maxLines: Int = 1,
    minLines: Int = 1
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = Color(0xFF374151),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    placeholder,
                    color = Color(0xFF9CA3AF)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF667eea),
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedTextColor = Color(0xFF1F2937),
                unfocusedTextColor = Color(0xFF1F2937),
                focusedContainerColor = Color(0xFF667eea).copy(alpha = 0.05f),
                unfocusedContainerColor = Color(0xFFF9FAFB)
            ),
            shape = RoundedCornerShape(16.dp),
            maxLines = maxLines,
            minLines = minLines
        )
    }
}

@Composable
fun StatusCreateChip(
    status: TaskStatus,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = 0.7f,
            stiffness = 300f
        ),
        label = "chip_scale"
    )

    FilterChip(
        onClick = onClick,
        label = {
            Text(
                status.name,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else Color(0xFF6B7280),
                fontSize = 14.sp
            )
        },
        selected = isSelected,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = color,
            selectedLabelColor = Color.White,
            containerColor = Color(0xFFF3F4F6),
            labelColor = Color(0xFF6B7280)
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = FilterChipDefaults.filterChipElevation(
            elevation = if (isSelected) 6.dp else 2.dp
        ),
        modifier = Modifier.scale(scale)
    )
}

@Composable
fun FloatingCreateElements() {
    val infiniteTransition = rememberInfiniteTransition(label = "floatingCreateElements")

    val float1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(3800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float1"
    )

    val float2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -22f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float2"
    )

    val float3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(4200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float3"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Círculo flotante 1
        Box(
            modifier = Modifier
                .size(70.dp)
                .offset(x = 70.dp, y = 50.dp + float1.dp)
                .background(
                    color = Color.White.copy(alpha = 0.12f),
                    shape = CircleShape
                )
        )

        // Círculo flotante 2
        Box(
            modifier = Modifier
                .size(45.dp)
                .offset(x = 290.dp, y = 70.dp + float2.dp)
                .background(
                    color = Color.White.copy(alpha = 0.08f),
                    shape = CircleShape
                )
        )

        // Círculo flotante 3
        Box(
            modifier = Modifier
                .size(60.dp)
                .offset(x = 40.dp, y = 120.dp + float3.dp)
                .background(
                    color = Color.White.copy(alpha = 0.06f),
                    shape = CircleShape
                )
        )
    }
}