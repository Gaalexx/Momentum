package com.project.momentum.features.editingAccount

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.project.momentum.R
import com.project.momentum.features.auth.models.NavEvent
import com.project.momentum.ui.assets.CancelButton
import com.project.momentum.ui.assets.ContinueButton
import com.project.momentum.ui.assets.TextFieldRegistration
import com.project.momentum.ui.assets.TopBarTemplate
import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.theme.ConstColours


@Composable
fun EditingAccountRoot(
    onBackClick: () -> Unit,
    onContinueClick: () ->Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: EditAccountViewModel = hiltViewModel()
    val uiState = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                NavEvent.NavigateToNextScreen -> onContinueClick()
                else -> onBackClick()
            }
        }
    }

    EditingAccountScreen(
        uiInfoState = uiState.value,
        onLoginChange = { viewModel.updateLogin(it) },
        onEmailChange = { viewModel.updateEmail(it) },
        onPhoneChange = { viewModel.updatePhone(it) },
        onBackClick = onBackClick,
        onContinueClick = { viewModel.next() },
        modifier = modifier
    )
}

@Composable
fun EditingAccountScreen(
    uiInfoState: EditAccountState,
    onLoginChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onContinueClick: () ->Unit,
    modifier: Modifier = Modifier
) {

    val subHeadLine: @Composable (String) -> Unit = {
        Text (
            text = it,
            style = AppTextStyles.SubHeadlines,
            color = ConstColours.MAIN_BRAND_BLUE,
        )
    }
    @Composable
    fun EditTextField(
        value: String,
        onValueChange: (String) -> Unit,
        placeholder: String? = null,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        )
    ) {
        TextFieldRegistration(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .height(48.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(36.dp),
                    spotColor = ConstColours.MAIN_BRAND_BLUE
                ),
            placeholder = placeholder,
            keyboardOptions = keyboardOptions,
        )
    }

    TopBarTemplate (
        label = R.string.label_editing,
        onBackClick = onBackClick,
        modifier = modifier
    ) {
        val iconTint = Color(0xFFEDEEF2) // TODO: fa fa fa what a faaa

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.medium_large_padding))
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(ConstColours.MAIN_BACK_GRAY)
                        .border(2.dp, ConstColours.MAIN_BRAND_BLUE, CircleShape)
                ) {
                    if (uiInfoState.fields.profilePhotoURL == null) {
//                        Image(
//                            painter = painterResource(R.drawable.profile_image_small),
//                            contentDescription = null,
//                            modifier = Modifier
//                                .height(80.dp)
//                                .aspectRatio(1f)
//                                .align(Alignment.Center),
//                        )
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = stringResource(R.string.account_avatar),
                            tint = iconTint.copy(alpha = 0.7f),
                            modifier = Modifier
                                .size(80.dp)
                                .align(Alignment.Center)
                        )
                    } else {
                        AsyncImage(
                            model = uiInfoState.fields.profilePhotoURL,
                            contentDescription = stringResource(R.string.account_avatar),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(2.dp)
                                .clip(CircleShape)
                        )
                    }
                }
            }
            subHeadLine("Основное")
            Divider(
                thickness = dimensionResource(R.dimen.thickness_divider),
                color = ConstColours.WHITE,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.medium_padding))
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.small_padding)
                ),
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.medium_padding))
            ) {
                subHeadLine("Логин")
                EditTextField(
                    value = uiInfoState.fields.username ?: "",
                    onValueChange = onLoginChange,
                )
                subHeadLine("Почта")
                EditTextField(
                    value = uiInfoState.fields.email ?: "",
                    onValueChange = onEmailChange,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )
                subHeadLine("Телефон")
                EditTextField(
                    value = uiInfoState.fields.phone ?: "",
                    onValueChange = onPhoneChange,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    )
                )
            }
            Spacer(Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.medium_padding)
                )
            ) {
                CancelButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .height(dimensionResource(R.dimen.sub_button_size))
                        .weight(1f),
                )
                ContinueButton(
                    text = "Сохранить",
                    onClick = onContinueClick,
                    modifier = Modifier
                        .height(dimensionResource(R.dimen.sub_button_size))
                        .weight(1f)
                )
            }
        }
    }
}

@Preview
@Composable
fun EditingAccountScreenPreview() {
    EditingAccountScreen(
        uiInfoState = EditAccountState.Content(EditAccountFields()),
        onLoginChange = {},
        onEmailChange = {},
        onPhoneChange = {},
        onBackClick = {},
        onContinueClick = {}
    )
}