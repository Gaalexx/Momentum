package com.project.momentum.ui.assets

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.PlayerSurface
import com.project.momentum.features.contentcreation.ui.assets.RecordingBorderProgress
import com.project.momentum.ui.theme.ConstColours
import kotlinx.coroutines.delay
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.min

@Composable
fun CircularSeekArea(
    modifier: Modifier = Modifier,
    onProgressChanged: (Float) -> Unit,
    content: @Composable () -> Unit
) {
    var sizePx by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .onSizeChanged { sizePx = it }
            .pointerInput(sizePx) {
                detectDragGestures(
                    onDragStart = { offset ->
                        updateCircularProgress(
                            touch = offset,
                            size = sizePx,
                            onProgressChanged = onProgressChanged
                        )
                    },
                    onDrag = { change, _ ->
                        updateCircularProgress(
                            touch = change.position,
                            size = sizePx,
                            onProgressChanged = onProgressChanged
                        )
                        change.consume()
                    }
                )
            }
    ) {
        content()
    }
}

private fun updateCircularProgress(
    touch: Offset,
    size: IntSize,
    onProgressChanged: (Float) -> Unit
) {
    if (size.width == 0 || size.height == 0) return

    val center = Offset(size.width / 2f, size.height / 2f)
    val dx = touch.x - center.x
    val dy = touch.y - center.y

    var angle = atan2(dy, dx)
    if (angle < 0) angle += 2f * PI.toFloat()

    val progress = angle / (2f * PI.toFloat())
    onProgressChanged(progress.coerceIn(0f, 1f))
}


@Composable
fun VideoPreviewBox(
    player: ExoPlayer,
    onSeekRequested: (Float) -> Unit
) {
    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(player) {
        while (true) {
            val positionMs = player.currentPosition
            val durationMs = player.duration
            progress = if (durationMs > 0L) {
                (positionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)
            } else {
                0f
            }
            if (progress > 0.999) {
                player.seekTo(0)
            }
            delay(16)
        }
    }

    CircularSeekArea(
        modifier = Modifier.fillMaxSize(),
        onProgressChanged = { newProgress ->
            onSeekRequested(newProgress)
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxWidth(0.95f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(60.dp))
                    .background(ConstColours.MAIN_BACK_GRAY)
                    .align(Alignment.Center)
            ) {
                PlayerSurface(
                    player = player,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            RecordingBorderProgress(
                progress = progress,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
fun VideoPreview(
    context: Context,
    uri: Uri
) {
    val player = remember(uri) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(player) {
        onDispose { player.release() }
    }

    VideoPreviewBox(
        player = player,
        onSeekRequested = { progress ->
            val duration = player.duration
            if (duration > 0) {
                val targetPosition = (duration * progress).toLong()
                player.seekTo(targetPosition)
            }
        }
    )
}

//@Composable
//fun VideoPreview(
//    context: Context,
//    uri: Uri
//) {
//    var positionMs by remember { mutableLongStateOf(0L) }
//
//    var progress by remember { mutableFloatStateOf(0f) }
//
//    val player = remember(uri) {
//        ExoPlayer.Builder(context).build().apply {
//            setMediaItem(MediaItem.fromUri(uri))
//            prepare()
//            playWhenReady = true
//        }
//    }
//
//    DisposableEffect(player) {
//        onDispose {
//            player.release()
//        }
//    }
//
//    LaunchedEffect(player) {
//        while (true) {
//            positionMs = player.currentPosition
//            val durationMs = player.duration
//            progress = if (durationMs > 0L) {
//                (positionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)
//            } else {
//                0f
//            }
//
//            if (progress > 0.999) {
//                player.seekTo(0)
//            }
//            delay(16)
//        }
//    }
//
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        Box(
//            Modifier
//                .fillMaxWidth(0.95f)
//                .aspectRatio(1f)
//                .clip(RoundedCornerShape(60.dp))
//                .background(ConstColours.MAIN_BACK_GRAY)
//                .align(Alignment.Center)
//        ) {
//            PlayerSurface(
//                player = player,
//                modifier = Modifier.fillMaxSize(),
//            )
//        }
//        RecordingBorderProgress(
//            progress = progress,
//            modifier = Modifier.fillMaxSize(),
//        )
//    }
//}
