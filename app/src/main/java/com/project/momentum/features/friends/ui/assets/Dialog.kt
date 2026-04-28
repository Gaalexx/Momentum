package com.project.momentum.features.friends.ui.assets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.PersonRemove
import androidx.compose.material.icons.outlined.RestoreFromTrash
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.project.momentum.R
import com.project.momentum.features.friends.ui.User
import com.project.momentum.features.friends.viewmodel.FriendsScreenEvent
import com.project.momentum.features.friends.viewmodel.SelectedIndex
import com.project.momentum.ui.assets.TextFieldRegistration
import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.theme.ConstColours


@Composable
fun SingleChoiceSegmentedButton(
    modifier: Modifier = Modifier,
    selectedIndex: SelectedIndex = SelectedIndex.EMAIL,
    onEvent: (FriendsScreenEvent) -> Unit
) {
    val options = listOf(
        stringResource(R.string.email),
        stringResource(R.string.login)
    )

    SingleChoiceSegmentedButtonRow(
        modifier = modifier
    ) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                colors = SegmentedButtonDefaults.colors(
                    activeContentColor = ConstColours.WHITE,
                    activeContainerColor = ConstColours.MAIN_BRAND_BLUE,
                    inactiveContainerColor = ConstColours.MAIN_BACK_GRAY,
                    inactiveContentColor = ConstColours.WHITE,
                    activeBorderColor = ConstColours.WHITE,
                    inactiveBorderColor = ConstColours.WHITE
                ),
                onClick = {
                    onEvent(
                        FriendsScreenEvent.ChangeSelectedIndex(
                            SelectedIndex.fromIndex(
                                index
                            )
                        )
                    )
                },
                selected = index == selectedIndex.index,
                label = {
                    Text(
                        label,
                        style = AppTextStyles.ButtonText
                    )
                }
            )
        }
    }
}

@Composable
fun AddFriendDialog(
    value: String,
    selectedIndex: SelectedIndex,
    onEvent: (FriendsScreenEvent) -> Unit = {},
    placeholder: String = "",
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorText: String? = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {

    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.4f)
            .clip(RoundedCornerShape(20.dp))
            .background(ConstColours.BLACK)
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
                    .weight(2f),
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
            Spacer(
                modifier = Modifier.weight(1f)
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Button(
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
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight(0.8f),
                    colors = ButtonColors(
                        containerColor = ConstColours.MAIN_BRAND_BLUE,
                        contentColor = ConstColours.WHITE,
                        disabledContentColor = ConstColours.MAIN_BRAND_BLUE_ALPHA40,
                        disabledContainerColor = ConstColours.WHITE
                    )
                ) {
                    Text(
                        text = stringResource(R.string.send_request),
                        style = AppTextStyles.ButtonText
                    )
                }
            }


        }

    }

}

@Composable
fun DeleteFriendDialog(
    onClick: () -> Unit = {}
) {
        Card(
            modifier = Modifier
                .size(width = 150.dp, height = 50.dp)
                .clip(RoundedCornerShape(20.dp))
                .clickable {
                    onClick()
                },
            backgroundColor = ConstColours.RED
        ) {
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.weight(1f),
                    imageVector = Icons.Outlined.PersonRemove,
                    contentDescription = "Корзина",
                    tint = ConstColours.WHITE
                )
                Text(
                    modifier = Modifier.weight(3f),
                    text = stringResource(R.string.delete),
                    color = ConstColours.WHITE,
                    style = AppTextStyles.SupportingText
                )
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
        AddFriendDialog(
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

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PreviewDeleteFriendDialog() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        DeleteFriendDialog()
    }
}
