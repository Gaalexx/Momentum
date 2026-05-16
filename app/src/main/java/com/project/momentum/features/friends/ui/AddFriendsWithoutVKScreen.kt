package com.project.momentum.features.friends.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.momentum.R
import com.project.momentum.features.friends.viewmodel.FriendsScreenEvent
import com.project.momentum.features.friends.viewmodel.FriendsViewModel
import com.project.momentum.features.friends.viewmodel.SelectedIndex
import com.project.momentum.ui.assets.ContinueButtonAdaptive
import com.project.momentum.ui.assets.SingleChoiceSegmentedButton
import com.project.momentum.ui.assets.TextFieldRegistration
import com.project.momentum.ui.assets.TopBarTemplate
import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.theme.ConstColours

@Composable
fun AddFriendsWithoutVKScreenRoute(
    onBackClick: () -> Unit,
    viewModel: FriendsViewModel = hiltViewModel(),
){
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent
    val errorState = uiState.errorState
    val errorTextId = uiState.errorText
    val addFriendQuery = uiState.addFriendQuery
    val selectedIndex = uiState.selectedIndex

    AddFriendsWithoutVKScreen(
        onBackClick = onBackClick,
        value = addFriendQuery,
        selectedIndex = selectedIndex,
        onEvent = onEvent,
        onValueChange = { onEvent(FriendsScreenEvent.AddFriendQueryChange(it)) },
        isError = errorState,
        errorText = if (errorTextId != null && errorState) stringResource(errorTextId) else ""
    )
}
@Composable
fun AddFriendsWithoutVKScreen(
    onBackClick: () -> Unit,
    value: String,
    selectedIndex: SelectedIndex,
    onEvent: (FriendsScreenEvent) -> Unit = {},
    placeholder: String = "",
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorText: String? = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    TopBarTemplate(
        label = R.string.friend_search,
        onBackClick = onBackClick,
        modifier = Modifier,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(ConstColours.BLACK),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.4f)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.friend_search),
                            color = ConstColours.WHITE,
                            style = AppTextStyles.Headlines
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        TextFieldRegistration(
                            value = value,
                            onValueChange = onValueChange,
                            isError = isError,
                            errorText = errorText,
                            placeholder = placeholder,
                            modifier = Modifier
                                .fillMaxWidth(0.8f),
                            keyboardOptions = keyboardOptions, // TODO: изменять в зависимости от selectedIndex
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        SingleChoiceSegmentedButton(
                            modifier = Modifier.fillMaxWidth(0.8f),
                            selectedIndex,
                            onEvent
                        )
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        ContinueButtonAdaptive(
                            onClick = {
                                when (selectedIndex) {
                                    SelectedIndex.EMAIL -> onEvent(
                                        FriendsScreenEvent.CreateFriendRequest.EmailRequest(
                                            value
                                        )
                                    )

                                    SelectedIndex.LOGIN -> onEvent(
                                        FriendsScreenEvent.CreateFriendRequest.LoginRequest(
                                            value
                                        )
                                    )

                                    else -> Unit
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.8f),
                            colors = ButtonColors(
                                containerColor = ConstColours.MAIN_BRAND_BLUE,
                                contentColor = ConstColours.WHITE,
                                disabledContentColor = ConstColours.MAIN_BRAND_BLUE_ALPHA40,
                                disabledContainerColor = ConstColours.WHITE
                            ),
                            text = stringResource(R.string.send_request)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PreviewPager() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AddFriendsWithoutVKScreen(
            onBackClick = {},
            "Что это",
            onEvent = {},
            selectedIndex = SelectedIndex.LOGIN,
            placeholder = "Введите имя",
            onValueChange = {},
            isError = true,
            errorText = "Ошибочка вышла"
        )
    }
}