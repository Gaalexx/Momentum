package com.project.momentum.features.settings.template

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
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
import com.project.momentum.ui.assets.ContinueButton
import com.project.momentum.R
import com.project.momentum.features.auth.ui.handlingErrorLogin
import com.project.momentum.features.settings.models.DeleteAccountState
import com.project.momentum.features.settings.ui.handlingErrorDelete
import com.project.momentum.features.settings.viewmodel.DeleteEvent
import com.project.momentum.ui.assets.GlassTextField
import com.project.momentum.ui.assets.TextFieldRegistration
import com.project.momentum.ui.assets.TopBarTemplate
import com.project.momentum.ui.common.LoadingOverlay
import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.theme.ConstColours

@Preview(showBackground = true)
@Composable
fun DeleteAccountCheckPasswordScreenPreview() {
    TemplateDeleteAccountCheckPassword(
        onBackClick = {},
        onEvent = {},
        state = DeleteAccountState(),
        onContinueClick = {},
        passwordRepetition = ""
    )
}

@Composable
fun TemplateDeleteAccountCheckPassword(
    state: DeleteAccountState,
    onEvent: (DeleteEvent)-> Unit,
    passwordRepetition: String,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopBarTemplate(
        label = R.string.label_delete_account,
        onBackClick = onBackClick,
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .background(ConstColours.BLACK)
                .padding(paddingValues)
//                .windowIn  setsPadding(WindowInsets.systemBars) ,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                if (state.isLoading) {
                    LoadingOverlay()
                } else {
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

                    GlassTextField(
//                    TextFieldRegistration(
                        value = state.userData.password,
                        onValueChange = {onEvent(DeleteEvent.updatePasswordFstTextField(it))},
//                        modifier = Modifier.height(dimensionResource(R.dimen.button_size)),
                        placeholder = stringResource(R.string.placeholder_password),
                        isError = state.isError,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                    )

                    Spacer(Modifier.height(dimensionResource(R.dimen.small_padding)))

                    GlassTextField(
//                    TextFieldRegistration(
                        value = passwordRepetition,
                        onValueChange = {onEvent(DeleteEvent.updatePasswordScdTextField(it))},
//                        modifier = Modifier.height(dimensionResource(R.dimen.button_size)),
                        placeholder = stringResource(R.string.placeholder_password_repetition),
                        isError = state.isError,
                        errorText = handlingErrorDelete(state),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
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
}