package com.example.tasks.src.features.notes.presentation.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tasks.src.features.notes.domain.models.Task

@Composable
fun TaskItem(
    task: Task,
    statusColor: Color,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(),
        label = "task_scale"
    )

    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(300),
        label = "arrow_rotation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header - Siempre visible
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = task.titulo,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = task.descripcion,
                        fontSize = 15.sp,
                        color = Color(0xFF666666),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Chip de estado
                    Surface(
                        color = statusColor,
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = task.status.name,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }

                    // Indicador de imagen si existe
                    if (task.image.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Tiene imagen",
                            tint = Color(0xFF666666),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Flecha para expandir/contraer
                    Icon(
                        imageVector = Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Contraer" else "Expandir",
                        tint = Color(0xFF666666),
                        modifier = Modifier
                            .size(26.dp)
                            .rotate(rotationAngle)
                    )
                }
            }

            // Contenido expandible
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300)),
                exit = shrinkVertically(
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                ) {
                    // Separador
                    Divider(
                        color = Color(0xFFE0E0E0),
                        thickness = 1.dp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Descripción completa
                    Text(
                        text = task.descripcion,
                        fontSize = 15.sp,
                        color = Color(0xFF666666),
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(bottom = 18.dp)
                    )

                    // Imagen como cover (si existe)
                    if (task.image.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFF5F5F5))
                        ) {
                            AsyncImage(
                                model = task.image,
                                contentDescription = "Imagen de la tarea",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            // Overlay sutil para mejorar la legibilidad
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Color.Black.copy(alpha = 0.1f)
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))
                    }

                    // Botones de acción
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Botón editar
                        FilledTonalButton(
                            onClick = onEditClick,
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = Color(0xFFF5F5F5),
                                contentColor = Color(0xFF666666)
                            ),
                            modifier = Modifier.height(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar tarea",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Editar",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // Botón eliminar
                        FilledTonalButton(
                            onClick = onDeleteClick,
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = Color(0xFFFFEBEE),
                                contentColor = Color(0xFFE57373)
                            ),
                            modifier = Modifier.height(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar tarea",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Eliminar",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}