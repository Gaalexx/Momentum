package com.project.momentum.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.momentum.ConstColours
import com.project.momentum.ContinueButton
import com.project.momentum.R
import com.project.momentum.ui.assets.TextFieldRegistration
import com.project.momentum.ui.assets.TopBarTemplate

@Composable
fun CreateAccountScreen(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = ConstColours.BLACK

    TopBarTemplate(
        label = R.string.label_create_account,
        onBackClick = onBackClick,
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .background(bg)
                .padding(paddingValues)
//                .windowInsetsPadding(WindowInsets.systemBars) ,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.weight(1f))

                Box {
                    Image(
                        painter = painterResource(R.drawable.profile_image_small),
                        contentDescription = null,
                        modifier = Modifier
                            .height(120.dp)
                            .aspectRatio(1f),
                    )
                    Image(
                        painter = painterResource(R.drawable.add_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(dimensionResource(R.dimen.small_padding))
                            .size(20.dp)
                            .border(
                                width = 2.dp,
                                color = ConstColours.BLACK,
                                shape = CircleShape
                            )
                    )
                }
                Spacer(Modifier.weight(4f))
            }

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                TextFieldRegistration(
                    //TODO: Сохрание + изменение состояния во viewModel
                    value = "qwertyuio",
                    onValueChange = {},
                    modifier = Modifier.height(dimensionResource(R.dimen.button_size))
                )
                Spacer(Modifier.height(dimensionResource(R.dimen.small_padding)))
                ContinueButton(
                    onClick = onContinueClick,
                    modifier = Modifier.height(dimensionResource(R.dimen.button_size))
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateAccountScreenPreview() {
    CreateAccountScreen(
        onBackClick = {},
        onContinueClick = {}
    )
}