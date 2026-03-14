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
import com.project.momentum.features.settings.models.DeleteAccountState
import com.project.momentum.ui.assets.TextFieldRegistration
import com.project.momentum.ui.assets.TopBarTemplate
import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.theme.ConstColours

@Preview(showBackground = true)
@Composable
fun DeleteAccountCheckPasswordScreenPreview() {
    TemplateDeleteAccountCheckPassword(
        onBackClick = {},
        onContinueClick = {},
        onPasswordChange = {},
        state = DeleteAccountState(),
    )
}

@Composable
fun TemplateDeleteAccountCheckPassword(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    onPasswordChange: (String) -> Unit,
    state: DeleteAccountState,
    modifier: Modifier = Modifier
) {
    TopBarTemplate(
        label = R.string.label_delete_account,
        onBackClick = onBackClick,
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = modifier
                .background(ConstColours.BLACK)
                .padding(paddingValues)
                .windowInsetsPadding(WindowInsets.systemBars),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
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
                    value = state.userData.password,
                    onValueChange = onPasswordChange,
                    modifier = Modifier.height(dimensionResource(R.dimen.button_size)),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                )
                Spacer(Modifier.height(dimensionResource(R.dimen.small_padding)))
                TextFieldRegistration(
                    value = state.userData.password,
                    onValueChange = onPasswordChange,
                    modifier = Modifier.height(dimensionResource(R.dimen.button_size)),
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