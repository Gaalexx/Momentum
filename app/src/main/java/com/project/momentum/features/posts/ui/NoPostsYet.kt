package com.project.momentum.features.posts.ui

import com.project.momentum.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.momentum.features.contentcreation.ui.assets.CameraTopBar
import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.theme.ConstColours

@Composable
fun NoPostsYet(
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ConstColours.BLACK),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(2f))
        Text(
            modifier = Modifier,
            text = stringResource(R.string.no_posts_yet),
            color = Color.White,
            style = AppTextStyles.MainText
        )
        Spacer(Modifier.weight(3f))
    }


}


@Preview(showBackground = true, backgroundColor = 0xFF0B0C0F)
@Composable
fun NoPostsYetPreview() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CameraTopBar(
            onProfileClick = {},
            onGoToSettings = {},
            onGoToFriends = {},
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 14.dp),
        )
        NoPostsYet()
    }
}