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
import com.project.momentum.features.contentcreation.ui.CameraPreviewContainer
import com.project.momentum.features.contentcreation.ui.CameraScreenState
import com.project.momentum.ui.theme.ConstColours

private val PreviewCardShape = RoundedCornerShape(60.dp)
private const val RecordingProgressStartShiftFraction = 0.3425f

@Composable
internal fun CameraPreviewCard(
    hasCameraPermission: Boolean,
    state: CameraScreenState,
    progress: Float,
    modifier: Modifier = Modifier,
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
            if (hasCameraPermission) {
                CameraPreviewContainer(
                    state = state,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                CameraPermissionPlaceholder()
            }
        }
    }
}

@Composable
private fun RecordingBorderProgress(
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
        val visibleLength = totalLength * progress
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
private fun CameraPermissionPlaceholder(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.PhotoCamera,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.35f),
            modifier = Modifier.size(56.dp),
        )
    }
}
