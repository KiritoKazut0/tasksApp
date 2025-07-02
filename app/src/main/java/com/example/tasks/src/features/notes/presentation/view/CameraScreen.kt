package com.example.tasks.src.features.notes.presentation.view

import android.Manifest
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tasks.src.core.di.HardwareModule
import com.example.tasks.src.features.notes.presentation.viewModel.CameraViewModel
import com.example.tasks.src.features.notes.presentation.viewModel.factory.CameraViewModelFactory
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.isGranted

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreenComponent(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    var isCameraFactoryInitialized by remember { mutableStateOf(false) }

    // Inicializar solo si se otorgó permiso
    LaunchedEffect(cameraPermissionState.status) {
        if (cameraPermissionState.status.isGranted && !isCameraFactoryInitialized) {
            HardwareModule.initCameraFactory(context, lifecycleOwner, previewView)
            isCameraFactoryInitialized = true
        }
    }

    // Solicitar permiso si no lo tiene
    if (!cameraPermissionState.status.isGranted) {
        LaunchedEffect(Unit) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    // Mostrar loading hasta que se inicialice correctamente
    if (!isCameraFactoryInitialized) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // Crear el ViewModel solo cuando el factory esté listo
    val viewModel: CameraViewModel = viewModel(
        factory = CameraViewModelFactory(HardwareModule.cameraFactory)
    )

    val showCamera by viewModel.showCamera.collectAsState()
    val capturedImages by viewModel.capturedImages.collectAsState()
    val showImagePreview by viewModel.showImagePreview.collectAsState()
    val isCapturing by viewModel.isCapturing.collectAsState()
    val captureError by viewModel.captureError.collectAsState()

    DisposableEffect(Unit) {
        onDispose { viewModel.releaseCamera() }
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Photos",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF666666)
            )
            Text(
                text = "${viewModel.getCapturedImagesCount()}/5",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF999999)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .size(80.dp)
                    .clickable {
                        if (cameraPermissionState.status.isGranted) {
                            viewModel.showCameraDialog()
                        } else {
                            cameraPermissionState.launchPermissionRequest()
                        }
                    },
                colors = CardDefaults.cardColors(
                    containerColor = if (viewModel.canAddMoreImages())
                        Color(0xFFF5F5F5) else Color(0xFFE0E0E0)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Camera,
                            contentDescription = "Add photo",
                            tint = if (viewModel.canAddMoreImages())
                                Color(0xFF666666) else Color(0xFFBBBBBB),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Add",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (viewModel.canAddMoreImages())
                                Color(0xFF666666) else Color(0xFFBBBBBB)
                        )
                    }
                }
            }

            capturedImages.take(4).forEachIndexed { index, imageUri ->
                Box(modifier = Modifier.size(80.dp)) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Captured image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { viewModel.showImagePreview(imageUri) },
                        contentScale = ContentScale.Crop
                    )

                    IconButton(
                        onClick = { viewModel.removeCapturedImage(index) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(24.dp)
                            .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Delete photo",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }

    captureError?.let {
        LaunchedEffect(it) { viewModel.clearError() }
    }

    if (showCamera && cameraPermissionState.status.isGranted) {
        Dialog(
            onDismissRequest = { viewModel.hideCameraDialog() },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                AndroidView(
                    factory = { previewView },
                    modifier = Modifier.fillMaxSize(),
                    update = { view -> view.post { view.requestLayout() } }
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.hideCameraDialog() },
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close camera",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.takePhoto() },
                        enabled = !isCapturing,
                        modifier = Modifier
                            .size(80.dp)
                            .background(
                                if (isCapturing) Color.Gray else Color.White,
                                CircleShape
                            )
                            .border(4.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                    ) {
                        if (isCapturing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(40.dp),
                                color = Color.Black
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Camera,
                                contentDescription = "Take photo",
                                tint = Color.Black,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.size(60.dp))
                }
            }
        }
    }

    showImagePreview?.let { imageUri ->
        Dialog(onDismissRequest = { viewModel.hideImagePreview() }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f))
                    .clickable { viewModel.hideImagePreview() }
            ) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Image preview",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentScale = ContentScale.Fit
                )

                IconButton(
                    onClick = { viewModel.hideImagePreview() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close preview",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
