package com.project.momentum.features.contentcreation.ui.assets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.project.momentum.features.contentcreation.models.ContentCreationMode
import com.project.momentum.features.contentcreation.state.CameraScreenState
import com.project.momentum.ui.assets.AudioRadialVisualizer
import com.project.momentum.ui.theme.ConstColours

private val PreviewCardShape = RoundedCornerShape(60.dp)
private const val RecordingProgressStartShiftFraction = 0.3425f


@Composable
private fun CameraInactivePlaceholder(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ConstColours.MAIN_BACK_GRAY),
    )
}


@Composable
internal fun MediaCreationPreviewCard(
    modifier: Modifier = Modifier,
    mode: ContentCreationMode,
    hasCameraPermission: Boolean,
    hasMicrophonePermission: Boolean,
    cameraState: CameraScreenState,
    progress: Float,
    audioLevel: Float,
    cameraPreviewEnabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(PreviewCardShape)
            .background(ConstColours.BLACK),
    ) {
        RecordingBorderProgress(
            progress = progress,
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .aspectRatio(1f)
                .clip(PreviewCardShape)
                .background(ConstColours.MAIN_BACK_GRAY)
                .align(Alignment.Center),
        ) {
            when (mode) {
                ContentCreationMode.Camera -> {
                    when {
                        !hasCameraPermission -> {
                            PermissionPlaceholder(iconMode = ContentCreationMode.Camera)
                        }

                        cameraPreviewEnabled -> {
                            CameraPreviewContainer(
                                state = cameraState,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }

                        else -> {
                            CameraInactivePlaceholder()
                        }
                    }
                }

                ContentCreationMode.Audio -> {
                    AudioRadialVisualizer(
                        level = audioLevel,
                        modifier = Modifier.fillMaxSize(),
                    )
                    if (!hasMicrophonePermission) {
                        PermissionPlaceholder(iconMode = ContentCreationMode.Audio)
                    }
                }
            }
        }
    }
}

@Composable
internal fun CameraPreviewCard(
    hasCameraPermission: Boolean,
    state: CameraScreenState,
    progress: Float,
    modifier: Modifier = Modifier,
) {
    MediaCreationPreviewCard(
        mode = ContentCreationMode.Camera,
        hasCameraPermission = hasCameraPermission,
        hasMicrophonePermission = true,
        cameraState = state,
        progress = progress,
        audioLevel = 0f,
        modifier = modifier,
    )
}

@Composable
fun RecordingBorderProgress(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    val progressPath = remember { PathMeasure() }

    Canvas(modifier = modifier) {
        val fullPath = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(
                        topLeft = Offset.Zero,
                        bottomRight = Offset(size.width, size.height),
                    ),
                    cornerRadius = CornerRadius(65.dp.toPx()),
                ),
                direction = Path.Direction.Clockwise,
            )
        }

        progressPath.setPath(fullPath, forceClosed = true)
        val totalLength = progressPath.length
        val start = totalLength * RecordingProgressStartShiftFraction
        val visibleLength = totalLength * progress.coerceIn(0f, 1f)
        val end = start + visibleLength
        val segmentPath = Path()

        if (end <= totalLength) {
            progressPath.getSegment(
                startDistance = start,
                stopDistance = end,
                destination = segmentPath,
                startWithMoveTo = true,
            )
        } else {
            progressPath.getSegment(
                startDistance = start,
                stopDistance = totalLength,
                destination = segmentPath,
                startWithMoveTo = true,
            )

            val secondPart = Path()
            progressPath.getSegment(
                startDistance = 0f,
                stopDistance = end - totalLength,
                destination = secondPart,
                startWithMoveTo = true,
            )
            segmentPath.addPath(secondPart)
        }

        drawPath(
            path = segmentPath,
            color = ConstColours.WHITE,
            style = Stroke(width = 8.dp.toPx()),
        )
    }
}

@Composable
private fun PermissionPlaceholder(
    iconMode: ContentCreationMode,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = when (iconMode) {
                ContentCreationMode.Camera -> Icons.Outlined.PhotoCamera
                ContentCreationMode.Audio -> Icons.Outlined.Mic
            },
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.35f),
            modifier = Modifier.size(56.dp),
        )
    }
}
