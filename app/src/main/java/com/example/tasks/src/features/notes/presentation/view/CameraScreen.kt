package com.example.tasks.src.features.notes.presentation.view

import android.Manifest
import android.net.Uri
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.tasks.src.features.notes.presentation.viewModel.CameraViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import androidx.compose.foundation.lazy.items
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreenComponent(
    cameraViewModel: CameraViewModel,
    previewView: PreviewView,
    modifier: Modifier = Modifier
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val scope = rememberCoroutineScope()

    // Estados
    val showCamera by cameraViewModel.showCamera.collectAsState()
    val capturedImages by cameraViewModel.capturedImages.collectAsState()
    val isCapturing by cameraViewModel.isCapturing.collectAsState()
    var showPermissionRationale by remember { mutableStateOf(false) }

    // Efectos
    LaunchedEffect(cameraPermissionState.status) {
        if (cameraPermissionState.status.shouldShowRationale) {
            showPermissionRationale = true
        }
    }

    // Diálogo de explicación de permisos
    if (showPermissionRationale) {
        AlertDialog(
            onDismissRequest = { showPermissionRationale = false },
            title = { Text("Permiso requerido") },
            text = { Text("Necesitamos acceso a la cámara para tomar fotos de tus tareas") },
            confirmButton = {
                Button(onClick = {
                    scope.launch {
                        cameraPermissionState.launchPermissionRequest()
                        showPermissionRationale = false
                    }
                }) {
                    Text("Entendido")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionRationale = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(modifier = modifier) {
        // Contador de fotos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Fotos tomadas", style = MaterialTheme.typography.labelMedium)
            Text("${capturedImages.size}/3", style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Área de fotos
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Botón para añadir foto
            item {
                PhotoAddButton(
                    enabled = capturedImages.size < 3,
                    onClick = {
                        if (cameraPermissionState.status.isGranted) {
                            cameraViewModel.showCameraDialog()
                        } else {
                            scope.launch {
                                cameraPermissionState.launchPermissionRequest()
                            }
                        }
                    }
                )
            }

            // Mostrar fotos existentes
            items(capturedImages) { uri ->
                PhotoThumbnail(
                    imageUri = uri,
                    onDelete = { cameraViewModel.removeCapturedImage(capturedImages.indexOf(uri)) }
                )
            }
        }
    }

    // Diálogo de la cámara
    if (showCamera && cameraPermissionState.status.isGranted) {
        Dialog(
            onDismissRequest = cameraViewModel::hideCameraDialog,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                // Vista previa de la cámara
                AndroidView(
                    factory = { previewView },
                    modifier = Modifier.fillMaxSize()
                )

                // Controles de la cámara
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Botón para cerrar
                    IconButton(
                        onClick = cameraViewModel::hideCameraDialog,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Cerrar cámara",
                            tint = Color.White
                        )
                    }

                    // Botón para capturar
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = { if (!isCapturing) cameraViewModel.takePhoto() },
                            modifier = Modifier
                                .size(72.dp)
                                .background(
                                    if (isCapturing) Color.Gray else Color.White,
                                    CircleShape
                                ),
                            enabled = !isCapturing
                        ) {
                            if (isCapturing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(36.dp),
                                    color = Color.Black,
                                    strokeWidth = 3.dp
                                )
                            } else {
                                Icon(
                                    Icons.Default.Camera,
                                    contentDescription = "Tomar foto",
                                    tint = Color.Black,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PhotoAddButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.size(100.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (enabled) MaterialTheme.colorScheme.surfaceVariant
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Añadir foto",
                    tint = if (enabled) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Añadir",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (enabled) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                )
            }
        }
    }
}

@Composable
private fun PhotoThumbnail(
    imageUri: Uri,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.size(100.dp)) {
        AsyncImage(
            model = imageUri,
            contentDescription = "Foto capturada",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        // Botón para eliminar
        IconButton(
            onClick = onDelete,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(28.dp)
                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Eliminar foto",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}