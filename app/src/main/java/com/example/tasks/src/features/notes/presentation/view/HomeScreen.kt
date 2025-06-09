package com.example.tasks.src.features.notes.presentation.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasks.R
import com.example.tasks.src.features.notes.data.di.AppModule
import com.example.tasks.src.features.notes.domain.models.Task
import com.example.tasks.src.features.notes.domain.models.TaskStatus
import com.example.tasks.src.features.notes.presentation.viewModel.DeleteTaskViewModel
import com.example.tasks.src.features.notes.presentation.viewModel.DeleteTaskViewModelFactory
import com.example.tasks.src.features.notes.presentation.viewModel.ListTaskViewModel
import com.example.tasks.src.features.notes.presentation.viewModel.ListTaskViewModelFactory



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

    val statusColors = mapOf(
        TaskStatus.CANCELADA to Color(0xFFE57373),
        TaskStatus.EN_PROGRESO to Color(0xFF64B5F6),
        TaskStatus.COMPLETADA to Color(0xFF81C784),
        TaskStatus.PENDIENTE to Color(0xFFBDBDBD)
    )

    LaunchedEffect(userId) {
        isVisible = true
        listTaskViewModel.fetchTasksForUser(userId)
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(0.8f),
                        painter = painterResource(R.drawable.font_app),
                        contentDescription = "Tasks Background",
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Color(0xFFF5F5F5).copy(alpha = 0.5f)
                            )
                    )

                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "List",
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

                        Text(
                            text = "${tasks.size} tareas",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF666666)
                        )
                    }
                }
            }

            // Error message
            errorMessage?.let { message ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFEBEE)
                    )
                ) {
                    Text(
                        text = message,
                        color = Color(0xFFD32F2F),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }


            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFFD9FF1D)
                    )
                }
            }

            // Lista de tareas
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = spring()
                ) + fadeIn()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(tasks) { task ->
                        TaskItem(
                            task = task,
                            statusColor = statusColors[task.status] ?: Color.Gray,
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

        // Botón flotante para crear nueva tarea
        FloatingActionButton(
            onClick = onCreateClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFFD9FF1D),
            contentColor = Color.Black
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Crear nueva tarea",
                modifier = Modifier.size(28.dp)
            )
        }
    }

    // Dialog de confirmación para eliminar
    if (showDeleteDialog && taskToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                taskToDelete = null
            },
            containerColor = Color.White,
            title = {
                Text(
                    text = "Eliminar Tarea",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas eliminar la tarea \"${taskToDelete?.titulo}\"?",
                    color = Color(0xFF666666)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        taskToDelete?.let { task ->
                            deleteTaskViewModel.deleteTask(task.id)
                            listTaskViewModel.removeTaskById(task.id)
                        }
                        showDeleteDialog = false
                        taskToDelete = null
                    }
                ) {
                    Text(
                        text = "Eliminar",
                        color = Color(0xFFE57373),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        taskToDelete = null
                    }
                ) {
                    Text(
                        text = "Cancelar",
                        color = Color(0xFF666666)
                    )
                }
            }
        )
    }
}
