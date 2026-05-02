package com.project.momentum.features.contentcreation.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.project.momentum.features.contentcreation.models.ContentCreationMode
import com.project.momentum.features.contentcreation.models.MediaTypeToSend

@Composable
fun RecorderScreen(
    onCameraClick: () -> Unit,
    onGoToFriends: () -> Unit,
    onProfileClick: () -> Unit,
    onGoToSettings: () -> Unit,
    onGoToPreview: (Uri, MediaTypeToSend) -> Unit,
    onGoToGallery: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MediaCreationScreen(
        modifier = modifier,
        initialMode = ContentCreationMode.Audio,
        onGoToPreview = onGoToPreview,
        onProfileClick = onProfileClick,
        onGoToGallery = onGoToGallery,
        onGoToSettings = onGoToSettings,
        onGoToFriends = onGoToFriends,
    )
}

@Preview
@Composable
private fun RecorderScreenPreview() {
    RecorderScreen(
        onCameraClick = {},
        onGoToFriends = {},
        onProfileClick = {},
        onGoToSettings = {},
        onGoToPreview = { _, _ -> },
        onGoToGallery = {},
    )
}
