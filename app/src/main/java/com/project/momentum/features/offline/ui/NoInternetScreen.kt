package com.project.momentum.features.offline.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.momentum.R
import com.project.momentum.ui.assets.ContinueButton
import com.project.momentum.ui.common.LoadingOverlay
import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.theme.ConstColours
import com.project.momentum.ui.theme.MomentumTheme

@Composable
fun NoInternetScreen(
    onRetryClick: () -> Unit = {},
    onOpenSettingsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ConstColours.BLACK)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(R.dimen.medium_padding)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Surface(
                shape = RoundedCornerShape(32.dp),
                color = ConstColours.MAIN_BACK_GRAY,
                tonalElevation = 0.dp,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = dimensionResource(R.dimen.medium_large_padding),
                        vertical = dimensionResource(R.dimen.large_padding)
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OfflineIllustration()

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.medium_padding)))

                    Text(
                        text = stringResource(R.string.offline_title),
                        style = AppTextStyles.Headlines,
                        color = ConstColours.WHITE,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_padding)))

                    Text(
                        text = stringResource(R.string.offline_description),
                        style = AppTextStyles.SupportingText,
                        color = ConstColours.SUPPORTING_TEXT,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.medium_padding)))

                    ContinueButton(
                        onClick = onRetryClick,
                        text = stringResource(R.string.offline_retry)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.offline_auto_refresh),
                style = AppTextStyles.SupportingText,
                color = ConstColours.SUPPORTING_TEXT,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.medium_padding))
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.medium_padding)))
        }

        if (isLoading) {
            LoadingOverlay()
        }
    }
}

@Composable
private fun OfflineIllustration(
    modifier: Modifier = Modifier,
) {
    val illustrationSize = 140.dp
    val iconSize = 72.dp

    Box(
        modifier = modifier.size(illustrationSize),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(illustrationSize)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            ConstColours.MAIN_BRAND_BLUE_ALPHA40,
                            ConstColours.TRANSPARENT_WHITE_ALPHA0
                        )
                    ),
                    shape = CircleShape
                )
        )

        Icon(
            imageVector = Icons.Outlined.WifiOff,
            contentDescription = null,
            tint = ConstColours.MAIN_BRAND_BLUE,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF181818)
@Composable
private fun NoInternetScreenPreview() {
        NoInternetScreen()
}
