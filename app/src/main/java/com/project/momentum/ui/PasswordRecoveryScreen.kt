package com.project.momentum.ui

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
fun PasswordRecoveryScreen(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    onSendCodeAgainClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEmail: Boolean = true,
) {
    val bg = ConstColours.BLACK

    TopBarTemplate(
        label = R.string.label_password_recovery,
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
                    text =
                        if (isEmail) stringResource(R.string.insert_email)
                         else stringResource(R.string.insert_phone_number),
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
                    value = "+79999999999",
                    onValueChange = {},
                    modifier = Modifier.height(dimensionResource(R.dimen.button_size)),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType =
                            if (isEmail) KeyboardType.Email
                            else KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    ),
                )
                Spacer(Modifier.height(dimensionResource(R.dimen.small_padding)))
                ContinueButton(
                    onClick = onContinueClick,
                    modifier = Modifier.height(dimensionResource(R.dimen.button_size))
                )

                Box(
                    modifier = Modifier
                        .clickable(
                            onClick = onSendCodeAgainClick
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text =
                            if (isEmail) stringResource(R.string.button_use_phone_number)
                            else stringResource(R.string.button_use_email),
                        modifier = Modifier.padding(dimensionResource(R.dimen.medium_padding)),
                        color = ConstColours.WHITE,
                        textAlign = TextAlign.Center,
                        style = AppTextStyles.SubButtonText,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PasswordRecoveryScreenPreview() {
    PasswordRecoveryScreen(
        onBackClick = {},
        onContinueClick = {},
        onSendCodeAgainClick = {}
    )
}