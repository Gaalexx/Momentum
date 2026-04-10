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


@Composable
fun AudioPreviewBox(
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

            }
            RecordingBorderProgress(
                progress = progress,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
fun AudioPreview(
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

    AudioPreviewBox(
        player = player,
        onSeekRequested = { progress ->
            val duration = player.duration
            if (duration > 0) {
                val targetPosition = (duration * progress).toLong()
                player.seekTo(targetPosition)
            }
        })
}

@Composable
fun AudioPreview(
    context: Context,
    uri: String
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

    AudioPreviewBox(
        player = player,
        onSeekRequested = { progress ->
            val duration = player.duration
            if (duration > 0) {
                val targetPosition = (duration * progress).toLong()
                player.seekTo(targetPosition)
            }
        })
}