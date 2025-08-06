package com.example.tasks.src.features.notes.presentation.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasks.src.features.notes.di.AppModule
import com.example.tasks.src.features.notes.domain.models.Task
import com.example.tasks.src.features.notes.domain.models.TaskStatus
import com.example.tasks.src.features.notes.presentation.viewModel.DeleteTaskViewModel
import com.example.tasks.src.features.notes.presentation.viewModel.factory.DeleteTaskViewModelFactory
import com.example.tasks.src.features.notes.presentation.viewModel.ListTaskViewModel
import com.example.tasks.src.features.notes.presentation.viewModel.factory.ListTaskViewModelFactory
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userId: String,
    onCreateClick: () -> Unit,
    onUpdateClick: (String) -> Unit
) {
    val listTaskViewModel: ListTaskViewModel = viewModel(
        factory = ListTaskViewModelFactory(AppModule.listTaskUseCase)
    )

    val deleteTaskViewModel: DeleteTaskViewModel = viewModel(
        factory = DeleteTaskViewModelFactory(AppModule.deleteTaskUseCase)
    )

    val tasks by listTaskViewModel.tasks.collectAsState()
    val errorMessage by listTaskViewModel.errorMessage.collectAsState()
    val isLoading by listTaskViewModel.isLoading.collectAsState()

    var isVisible by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var taskToDelete by remember { mutableStateOf<Task?>(null) }
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

    val statusColors = mapOf(
        TaskStatus.CANCELADA to Color(0xFFE57373),
        TaskStatus.EN_PROGRESO to Color(0xFF64B5F6),
        TaskStatus.COMPLETADA to Color(0xFF81C784),
        TaskStatus.PENDIENTE to Color(0xFFBDBDBD)
    )

    LaunchedEffect(userId) {
        delay(300)
        isVisible = true
        startAnimations = true
        listTaskViewModel.fetchTasksForUser(userId)
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
            modifier = Modifier.fillMaxSize()
        ) {
            // Header moderno con gradiente
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .alpha(headerAlpha)
            ) {
                // Elementos flotantes de fondo
                FloatingHomeElements()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Espaciador superior
                    Spacer(modifier = Modifier.height(16.dp))

                    // Título de la pantalla
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
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
                                        imageVector = Icons.Default.List,
                                        contentDescription = "Task List",
                                        tint = Color(0xFF667eea),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = "My",
                                    fontSize = 18.sp,
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontWeight = FontWeight.Light
                                )
                                Text(
                                    text = "Tasks",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        // Contador de tareas
                        Surface(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(
                                text = "${tasks.size} tasks",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Mensaje de error moderno
            errorMessage?.let { message ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
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
                        Surface(
                            color = Color(0xFFFF6B6B),
                            shape = CircleShape,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "!",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
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

            // Loading moderno
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color(0xFF667eea),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "Loading tasks...",
                                color = Color(0xFF374151),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Lista de tareas con animación
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = spring()
                ) + fadeIn()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(tasks.size) { index ->
                        val task = tasks[index]
                        ModernTaskItem(
                            task = task,
                            statusColor = statusColors[task.status] ?: Color.Gray,
                            index = index,
                            onEditClick = {
                                onUpdateClick(task.id)
                            },
                            onDeleteClick = {
                                taskToDelete = task
                                showDeleteDialog = true
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }

        // Botón flotante moderno
        FloatingActionButton(
            onClick = onCreateClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = Color(0xFF667eea),
            contentColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 12.dp,
                pressedElevation = 6.dp
            )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create new task",
                modifier = Modifier.size(24.dp)
            )
        }
    }

    // Dialog de confirmación moderno
    if (showDeleteDialog && taskToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                taskToDelete = null
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color(0xFFFF6B6B).copy(alpha = 0.1f),
                        shape = CircleShape,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "!",
                                color = Color(0xFFFF6B6B),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Delete Task",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF374151)
                    )
                }
            },
            text = {
                Text(
                    text = "Are you sure you want to delete \"${taskToDelete?.titulo}\"? This action cannot be undone.",
                    color = Color(0xFF6B7280),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        taskToDelete?.let { task ->
                            deleteTaskViewModel.deleteTask(task.id)
                            listTaskViewModel.removeTaskById(task.id)
                        }
                        showDeleteDialog = false
                        taskToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6B6B),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Delete",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showDeleteDialog = false
                        taskToDelete = null
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF6B7280)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Cancel",
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        )
    }
}

@Composable
fun ModernTaskItem(
    task: Task,
    statusColor: Color,
    index: Int,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(index * 100L)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { it / 3 },
            animationSpec = spring()
        ) + fadeIn()
    ) {
        TaskItem(
            task = task,
            statusColor = statusColor,
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick
        )
    }
}

@Composable
fun FloatingHomeElements() {
    val infiniteTransition = rememberInfiniteTransition(label = "floatingHomeElements")

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

    val rotate by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing)
        ),
        label = "rotate"
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

        // Elemento rotatorio
        Box(
            modifier = Modifier
                .size(30.dp)
                .offset(x = 320.dp, y = 120.dp)
                .background(
                    color = Color.White.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(15.dp)
                )
        )
    }
}