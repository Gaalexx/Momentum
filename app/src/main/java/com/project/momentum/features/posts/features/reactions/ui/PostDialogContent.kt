package com.project.momentum.features.posts.features.reactions.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.HideImage
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.project.momentum.R
import com.project.momentum.ui.assets.DialogEventButton
import com.project.momentum.ui.assets.PostDialogInfo
import com.project.momentum.ui.theme.ConstColours

@Composable
fun PostDialogContent(
    onHidePost: () -> Unit,
    onDeletePost: () -> Unit,
    isOwner: Boolean,
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10))
            .background(ConstColours.MAIN_BACK_GRAY)
            .width(IntrinsicSize.Max),
    ) {
        DialogEventButton(
            text = R.string.button_hide_post_for_me,
            icon = Icons.Outlined.HideImage,
            onClick = onHidePost
        )
        if (isOwner) {
            DialogEventButton(
                text = R.string.button_delete_post,
                icon = Icons.Outlined.Delete,
                onClick = onDeletePost,
                textColor = ConstColours.DELETE
            )
        }
    }
}