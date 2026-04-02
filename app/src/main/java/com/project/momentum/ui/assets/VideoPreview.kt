package com.project.momentum.ui.assets

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.media3.ui.compose.PlayerSurface

@Composable
fun VideoPreview(
    context: Context,
    uri: Uri
) {
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = true
        }
    }

//    AndroidView(
//        factory = { ctx ->
//            PlayerView(ctx).apply {
//                this.player = player
//            }
//        }
//    )

    PlayerSurface(
        player = player
    )
}