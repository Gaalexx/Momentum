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
import com.project.momentum.ui.assets.TemplateAuthorizationScreen
import com.project.momentum.ui.assets.TextFieldRegistration
import com.project.momentum.ui.assets.TopBarTemplate
import com.project.momentum.ui.theme.AppTextStyles

@Composable
fun PasswordRecoveryScreen(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    onSendCodeAgainClick: () -> Unit,
    modifier: Modifier = Modifier,
    isUsingEmail: Boolean = true,
) {
    TemplateAuthorizationScreen(
        label = R.string.label_password_recovery,
        title =
            if (isUsingEmail) R.string.insert_email
            else R.string.insert_phone_number,
        subButtonText =
            if (isUsingEmail) R.string.button_use_phone_number
            else R.string.button_use_email,
        onBackClick = onBackClick,
        onContinueClick = onContinueClick,
        onSubButtonClick = onSendCodeAgainClick,
        modifier = modifier,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType =
                if (isUsingEmail) KeyboardType.Email
                else KeyboardType.Phone,
            imeAction = ImeAction.Done
        ),
    )
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