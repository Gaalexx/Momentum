package com.project.momentum.features.settings.template

import androidx.compose.foundation.background
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.project.momentum.ui.assets.ContinueButton
import com.project.momentum.R
import com.project.momentum.features.auth.models.LoginType
import com.project.momentum.features.auth.ui.handlingErrorLogin
import com.project.momentum.features.settings.models.DeleteAccountState
import com.project.momentum.features.settings.ui.handlingErrorDelete
import com.project.momentum.features.settings.viewmodel.DeleteEvent
import com.project.momentum.ui.assets.TemplateAuthorizationScreen
import com.project.momentum.ui.assets.TextFieldRegistration
import com.project.momentum.ui.assets.TopBarTemplate
import com.project.momentum.ui.common.LoadingOverlay
import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.theme.ConstColours

@Preview(showBackground = true)
@Composable
fun DeleteAccountCheckCodeScreenPreview() {
    TemplateDeleteAccountCheckCode(
        onBackClick = {},
        onContinueClick = {},
        onEvent = {},
        state = DeleteAccountState(),
    )
}

@Composable
fun TemplateDeleteAccountCheckCode(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    onEvent: (DeleteEvent)-> Unit,
    state: DeleteAccountState,
    modifier: Modifier = Modifier
) {
    if (state.isLoading) {
        LoadingOverlay()
    } else {
        TemplateAuthorizationScreen(
            value = state.userData.verificationCode,
            label = R.string.label_delete_account,
            title = R.string.insert_code_email,
            subButtonText = R.string.button_send_code_again,
            onValueChange = {onEvent(DeleteEvent.updateUserCode(it))},
            onBackClick = onBackClick,
            onContinueClick = {onEvent(DeleteEvent.nextStep)},
            onSubButtonClick = {onEvent(DeleteEvent.sendCodeAgain)},
            modifier = modifier,
            placeholder = stringResource(R.string.placeholder_code),
            isError = state.isError,
            errorText = handlingErrorDelete(state),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            continueColors = ButtonDefaults.buttonColors(
                containerColor = ConstColours.ERROR_RED,
                contentColor = ConstColours.WHITE
            ),
            continueText = stringResource(R.string.button_delete_account)
        )
    }
}
