package com.example.tasks.src.features.notes.presentation.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasks.src.features.notes.di.AppModule
import com.example.tasks.src.features.notes.domain.models.TaskStatus
import com.example.tasks.src.features.notes.presentation.viewModel.UpdateTaskViewModel
import com.example.tasks.src.features.notes.presentation.viewModel.factory.UpdateTaskViewModelFactory
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateTaskScreen(
    taskId: String,
    userId: String,
    onNavigateBack: () -> Unit = {}
) {
    val viewModel: UpdateTaskViewModel = viewModel(
        factory = UpdateTaskViewModelFactory(AppModule.updateTaskUseCase)
    )

    val title by viewModel.title.collectAsState()
    val description by viewModel.description.collectAsState()
    val selectedStatus by viewModel.selectedStatus.collectAsState()
    val success by viewModel.success.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val isVisible by viewModel.isVisible.collectAsState()
    val titleExpanded by viewModel.titleExpanded.collectAsState()
    val descriptionExpanded by viewModel.descriptionExpanded.collectAsState()
    val showSuccess by viewModel.showSuccess.collectAsState()

    // Animaciones de entrada
    var startAnimations by remember { mutableStateOf(false) }

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

    val options = TaskStatus.entries

    val statusColors = mapOf(
        TaskStatus.CANCELADA to Color(0xFFE57373),
        TaskStatus.EN_PROGRESO to Color(0xFF64B5F6),
        TaskStatus.COMPLETADA to Color(0xFF81C784),
        TaskStatus.PENDIENTE to Color(0xFFBDBDBD)
    )

    LaunchedEffect(Unit) {
        delay(300)
        viewModel.setVisible(true)
        startAnimations = true
    }

    LaunchedEffect(success) {
        if (success) {
            delay(3000)
            onNavigateBack()
        }
    }

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
                FloatingUpdateElements()

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
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Update Task",
                                        tint = Color(0xFF667eea),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = "Update",
                                    fontSize = 18.sp,
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontWeight = FontWeight.Light
                                )
                                Text(
                                    text = "Task",
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
                    ModernUpdateCard(isExpanded = titleExpanded) {
                        ModernUpdateTextField(
                            value = title,
                            onValueChange = { viewModel.onTitleChanged(it) },
                            label = "Task Title",
                            placeholder = "What needs to be updated?"
                        )
                    }

                    // Tarjeta de estado con chips animados
                    ModernUpdateCard(isExpanded = false) {
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

                                    StatusUpdateChip(
                                        status = option,
                                        isSelected = isSelected,
                                        color = statusColors[option] ?: Color.Gray,
                                        onClick = { viewModel.onStatusSelected(option) }
                                    )
                                }
                            }
                        }
                    }

                    // Tarjeta de descripción
                    ModernUpdateCard(isExpanded = descriptionExpanded) {
                        ModernUpdateTextField(
                            value = description,
                            onValueChange = { viewModel.onDescriptionChanged(it) },
                            label = "Description",
                            placeholder = "Update task description...",
                            maxLines = 4,
                            minLines = 3
                        )
                    }

                    // Botón de actualizar con estilo moderno
                    Button(
                        onClick = {
                            viewModel.updateTask(idTask = taskId)
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
                                imageVector = Icons.Default.Check,
                                contentDescription = "Update",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Update Task",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
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
                        text = "Task updated successfully!",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }

    LaunchedEffect(showSuccess) {
        if (showSuccess) {
            delay(3000)
            viewModel.setShowSuccess(false)
        }
    }
}

@Composable
fun ModernUpdateCard(
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
fun ModernUpdateTextField(
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
fun StatusUpdateChip(
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
fun FloatingUpdateElements() {
    val infiniteTransition = rememberInfiniteTransition(label = "floatingUpdateElements")

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