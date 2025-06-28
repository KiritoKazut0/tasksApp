package com.example.tasks.src.features.notes.presentation.view

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasks.R
import com.example.tasks.src.features.notes.di.AppModule
import com.example.tasks.src.features.notes.domain.models.TaskStatus
import com.example.tasks.src.features.notes.presentation.viewModel.UpdateTaskViewModel
import com.example.tasks.src.features.notes.presentation.viewModel.UpdateTaskViewModelFactory
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
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

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
                            text = "Update",
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

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (titleExpanded) 8.dp else 2.dp
                        )
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
                                onValueChange = { viewModel.onTitleChanged(it) },
                                modifier = Modifier
                                    .fillMaxWidth(),
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
                                        onClick = { viewModel.onStatusSelected(option) },
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


                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (descriptionExpanded) 8.dp else 2.dp
                        )
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
                                onValueChange = { viewModel.onDescriptionChanged(it) },
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


                    Button(
                        onClick = {
                            viewModel.updateTask(idTask =taskId)
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
                }
            }
        }


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
                        text = "¡Tarea Actualizada exitosamente!",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
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