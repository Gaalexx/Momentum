package com.project.momentum.ui.assets

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.momentum.ConstColours
import com.project.momentum.R
import com.project.momentum.ui.theme.AppTextStyles

@Composable
fun TemplateAuthorizationScreen(
    value: String,
    @StringRes label: Int,
    @StringRes title: Int,
    @StringRes subButtonText: Int,
    onValueChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    onSubButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    val bg = ConstColours.BLACK

    TopBarTemplate(
        label = label,
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
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(title),
                    color = ConstColours.WHITE,
                    textAlign = TextAlign.Center,
                    style = AppTextStyles.Headlines,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(R.dimen.medium_padding))
                )
                Spacer(Modifier.height(dimensionResource(R.dimen.small_padding)))
                TextFieldRegistration(
                    value = value,
                    onValueChange = onValueChange,

                    modifier = Modifier.height(dimensionResource(R.dimen.button_size)),
                    keyboardOptions = keyboardOptions,
                )
                Spacer(Modifier.height(dimensionResource(R.dimen.small_padding)))
                ContinueButton(
                    onClick = onContinueClick,
                    modifier = Modifier.height(dimensionResource(R.dimen.button_size))
                )

                SubButton(
                    text = subButtonText,
                    onClick = onSubButtonClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TemplateAuthorizationScreenPreview() {
    TemplateAuthorizationScreen(
        value = "input text",
        label = R.string.template_label,
        title = R.string.template_page_title,
        subButtonText = R.string.template_sub_button,
        onValueChange = {},
        onBackClick = {},
        onContinueClick = {},
        onSubButtonClick = {}
    )
}