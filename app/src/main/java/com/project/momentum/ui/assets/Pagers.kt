package com.project.momentum.ui.assets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.momentum.R
import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.theme.ConstColours


@Composable
fun SingleChoiceSegmentedButton(modifier: Modifier = Modifier) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("Почта", "Номер", "Логин")

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
                onClick = { selectedIndex = index },
                selected = index == selectedIndex,
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
fun AddFriendPage(
    value: String,
    placeholder: String = "",
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
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
                    text = stringResource(com.project.momentum.R.string.friend_search),
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
                    placeholder = placeholder,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight(0.8f),
                    keyboardOptions = keyboardOptions,
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                SingleChoiceSegmentedButton(modifier = Modifier.fillMaxWidth(0.8f))
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
                    onClick = {},
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

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PreviewPager() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AddFriendPage("Что это", "Введите имя", {})
    }
}
