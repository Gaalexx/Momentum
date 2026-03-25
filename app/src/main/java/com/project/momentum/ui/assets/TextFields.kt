package com.project.momentum.ui.assets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.theme.ConstColours

@Composable
fun TextFieldRegistration(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isError: Boolean = false,
    errorText: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    val white09 = ConstColours.WHITE.copy(alpha = 0.9f)
    val white08 = ConstColours.WHITE.copy(alpha = 0.8f)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth(),
        supportingText = {
            if (isError && errorText != null) {
                Text(
                    text = errorText,
                    modifier = Modifier.fillMaxWidth(),
                    style = AppTextStyles.SupportingText,
                    textAlign = TextAlign.Center
                )
            }
        },
        placeholder = { if (placeholder != null)
            Text (
                text = placeholder,
                modifier = Modifier.fillMaxWidth(),
                style = AppTextStyles.MainText,
                textAlign = TextAlign.Center
            )
        },
        textStyle = AppTextStyles.MainText.copy(
            textAlign = TextAlign.Center
        ),
        isError = isError,
        keyboardOptions = keyboardOptions,
        maxLines = 1,
        shape = RoundedCornerShape(36.dp),
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = ConstColours.BLACK,
            focusedTextColor = ConstColours.BLACK,
            unfocusedTextColor = ConstColours.BLACK,
            errorTextColor = ConstColours.ERROR_RED,

            disabledBorderColor = white09,
            focusedBorderColor = white09,
            unfocusedBorderColor = white08,

            disabledContainerColor = white09,
            focusedContainerColor = white09,
            unfocusedContainerColor = white08,
            errorContainerColor = white09,

            errorSupportingTextColor = ConstColours.ERROR_RED,
        )
    )
}

@Preview(showBackground = false)
@Composable
fun TextFieldRegistrationPreview() {
    TextFieldRegistration(
        value = "",
        onValueChange = {},
        placeholder = "Введите текст...",
        errorText = "Ошибочка",
        isError = true
    )
}