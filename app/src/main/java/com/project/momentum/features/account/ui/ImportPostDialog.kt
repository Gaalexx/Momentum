package com.project.momentum.features.account.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.HideImage
import androidx.compose.material.icons.outlined.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.project.momentum.R
import com.project.momentum.ui.assets.DialogEventButton
import com.project.momentum.ui.theme.ConstColours

@Preview
@Composable
fun ImportPostDialogPreview() {
    ImportPostDialog(addButtonActions = AddButtonActions())
}

@Composable
fun ImportPostDialog(
    addButtonActions: AddButtonActions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10))
            .background(ConstColours.MAIN_BACK_GRAY)
            .width(IntrinsicSize.Max),
    ) {
        DialogEventButton(
            text = R.string.button_import_media_from_vk,
            image = painterResource(R.drawable.vk_logo),
            onClick = addButtonActions.onImportPostFromVk
        )
        DialogEventButton(
            text = R.string.button_import_media_from_device,
            icon = Icons.Outlined.Image,
            onClick = addButtonActions.onImportPostFromDevice
        )
        DialogEventButton(
            text = R.string.button_go_to_camera,
            icon = Icons.Outlined.CameraAlt,
            onClick = addButtonActions.onGoToCamera
        )

    }
}