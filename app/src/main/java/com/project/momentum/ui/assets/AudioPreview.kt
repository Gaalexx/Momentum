package com.project.momentum.ui.assets

import android.content.Context
import android.media.audiofx.Visualizer
import android.net.Uri
import android.util.Log
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.project.momentum.features.contentcreation.ui.assets.RecordingBorderProgress
import com.project.momentum.ui.theme.ConstColours
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sqrt

private const val TAG = "AudioPreview"

@Composable
fun AudioPreviewBox(
    player: ExoPlayer,
    onSeekRequested: (Float) -> Unit,
    isEditable: Boolean = true,
    visualLevel: Float = 0f,
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
            delay(16)
        }
    }

    CircularSeekArea(
        modifier = Modifier.fillMaxSize(),
        onProgressChanged = { newProgress ->
            onSeekRequested(newProgress)
        },
        isEditable = isEditable,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxWidth(0.95f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(60.dp))
                    .background(ConstColours.MAIN_BACK_GRAY)
                    .align(Alignment.Center),
            ) {
                AudioRadialVisualizer(
                    level = visualLevel,
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
fun AudioPreview(
    context: Context,
    uri: Uri,
) {
    val isLifecycleStarted = rememberIsLifecycleStarted()
    val player = remember(uri) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }
    val visualLevel = rememberPlaybackVisualizerLevel(
        player = player,
        enabled = isLifecycleStarted,
    )

    LaunchedEffect(player, isLifecycleStarted) {
        if (isLifecycleStarted) {
            player.play()
        } else {
            player.pause()
        }
    }

    DisposableEffect(player) {
        onDispose { player.release() }
    }

    AudioPreviewBox(
        player = player,
        visualLevel = visualLevel,
        onSeekRequested = { progress ->
            val duration = player.duration
            if (duration > 0) {
                val targetPosition = (duration * progress).toLong()
                player.seekTo(targetPosition)
            }
        },
    )
}

@Composable
fun AudioView(
    context: Context,
    uri: String,
    isEditable: Boolean = true,
    isPlaying: Boolean = true,
) {
    val isLifecycleStarted = rememberIsLifecycleStarted()
    val player = remember(uri) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }
    val shouldPlay = isPlaying && isLifecycleStarted
    val visualLevel = rememberPlaybackVisualizerLevel(
        player = player,
        enabled = shouldPlay,
    )

    LaunchedEffect(player, shouldPlay) {
        if (shouldPlay) {
            player.play()
        } else {
            player.pause()
        }
    }

    DisposableEffect(player) {
        onDispose { player.release() }
    }

    AudioPreviewBox(
        player = player,
        visualLevel = visualLevel,
        onSeekRequested = { progress ->
            val duration = player.duration
            if (duration > 0) {
                val targetPosition = (duration * progress).toLong()
                player.seekTo(targetPosition)
            }
        },
        isEditable = isEditable,
    )
}

@Composable
private fun rememberPlaybackVisualizerLevel(
    player: ExoPlayer,
    enabled: Boolean,
): Float {
    val scope = rememberCoroutineScope()
    var level by remember(player) { mutableFloatStateOf(0f) }
    var currentAudioSessionId by remember(player) { mutableIntStateOf(player.audioSessionId) }

    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onAudioSessionIdChanged(audioSessionId: Int) {
                currentAudioSessionId = audioSessionId
            }
        }

        player.addListener(listener)
        currentAudioSessionId = player.audioSessionId

        onDispose {
            player.removeListener(listener)
        }
    }

    DisposableEffect(currentAudioSessionId, enabled) {
        if (!enabled || currentAudioSessionId == C.AUDIO_SESSION_ID_UNSET) {
            level = 0f
            onDispose { }
        } else {
            var visualizer: Visualizer? = null
            try {
                val newVisualizer = Visualizer(currentAudioSessionId)
                visualizer = newVisualizer
                newVisualizer.apply {
                    captureSize = Visualizer.getCaptureSizeRange()[1]
                    setDataCaptureListener(
                        object : Visualizer.OnDataCaptureListener {
                            override fun onWaveFormDataCapture(
                                visualizer: Visualizer?,
                                waveform: ByteArray?,
                                samplingRate: Int,
                            ) {
                                val nextLevel = waveform?.toRmsLevel() ?: 0f
                                scope.launch {
                                    level = nextLevel
                                }
                            }

                            override fun onFftDataCapture(
                                visualizer: Visualizer?,
                                fft: ByteArray?,
                                samplingRate: Int,
                            ) = Unit
                        },
                        Visualizer.getMaxCaptureRate() / 2,
                        true,
                        false,
                    )
                    this.enabled = true
                }
            } catch (e: RuntimeException) {
                Log.w(TAG, "Unable to attach playback visualizer", e)
                level = 0f
            } catch (e: SecurityException) {
                Log.w(TAG, "Playback visualizer permission denied", e)
                level = 0f
            }

            onDispose {
                visualizer?.let { activeVisualizer ->
                    runCatching { activeVisualizer.enabled = false }
                    runCatching { activeVisualizer.release() }
                }
            }
        }
    }

    return level
}

private fun ByteArray.toRmsLevel(): Float {
    if (isEmpty()) return 0f

    var sum = 0.0
    for (byte in this) {
        val centered = (byte.toInt() and 0xFF) - 128
        sum += centered * centered
    }

    return (sqrt(sum / size) / 128f).toFloat().coerceIn(0f, 1f)
}
