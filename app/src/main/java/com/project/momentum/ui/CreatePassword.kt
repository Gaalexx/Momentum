package com.project.momentum.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.momentum.ConstColours
import com.project.momentum.ContinueButton
import com.project.momentum.R
import com.project.momentum.ui.assets.TextFieldRegistration
import com.project.momentum.ui.assets.TopBarTemplate
import com.project.momentum.ui.theme.AppTextStyles

@Composable
fun CreatePasswordScreen(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = ConstColours.BLACK

    TopBarTemplate(
        title = R.string.label_create_account,
        onBackClick = onBackClick,
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .background(bg)
                .padding(paddingValues)
//                .windowIn  setsPadding(WindowInsets.systemBars) ,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.insert_password),
                    color = ConstColours.WHITE,
                    textAlign = TextAlign.Center,
                    style = AppTextStyles.Headlines,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(R.dimen.medium_padding))
                )
                Spacer(Modifier.height(dimensionResource(R.dimen.small_padding)))
                TextFieldRegistration(
                    //TODO: Сохрание + изменение состояния во viewModel
                    value = "qwertyuio",
                    onValueChange = {},
                    modifier = Modifier.height(dimensionResource(R.dimen.button_size))
                )
                Spacer(Modifier.height(dimensionResource(R.dimen.small_padding)))
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
fun CreatePasswordScreenPreview() {
    CreatePasswordScreen(
        onBackClick = {},
        onContinueClick = {}
    )
}