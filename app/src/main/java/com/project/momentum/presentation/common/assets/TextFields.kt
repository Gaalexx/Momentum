package com.project.momentum.presentation.common.assets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.momentum.R
import com.project.momentum.presentation.common.theme.AppTextStyles
import com.project.momentum.presentation.common.theme.ConstColours

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
        modifier = modifier.fillMaxWidth(),
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
        textStyle = AppTextStyles.MainText.copy(textAlign = TextAlign.Center),
        isError = isError,
        keyboardOptions = keyboardOptions,
        maxLines = 1,
        shape = RoundedCornerShape(dimensionResource(R.dimen.control_corner_radius)),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = ConstColours.BLACK,
            unfocusedTextColor = ConstColours.BLACK,
            errorTextColor = ConstColours.ERROR_RED,
            focusedBorderColor = white09,
            unfocusedBorderColor = white08,
            focusedContainerColor = white09,
            unfocusedContainerColor = white08,
            errorContainerColor = white09,
        )
    )
}

@Composable
fun GlassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isError: Boolean = false,
    errorText: String? = null,
    textColor: Color = ConstColours.WHITE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val accentColor = if (isError) ConstColours.ERROR_RED else textColor
    val currentBorderColor = if (isFocused) accentColor else accentColor.copy(alpha = 0.6f)

    val shape = RoundedCornerShape(dimensionResource(R.dimen.control_corner_radius))
    val containerColor = textColor.copy(alpha = if (isFocused) 0.16f else 0.10f)

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        interactionSource = interactionSource,
        textStyle = AppTextStyles.MainText.copy(
            color = textColor,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = keyboardOptions,
        singleLine = true,
        cursorBrush = SolidColor(textColor),
        decorationBox = { innerTextField ->
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(containerColor, shape)
                        .border(width = 1.dp, color = currentBorderColor, shape = shape)
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (value.isEmpty() && placeholder != null) {
                        Text(
                            text = placeholder,
                            style = AppTextStyles.MainText,
                            color = textColor.copy(alpha = 0.4f),
                            textAlign = TextAlign.Center
                        )
                    }

                    innerTextField()
                }

                if (isError && errorText != null) {
                    Text(
                        text = errorText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        style = AppTextStyles.SupportingText,
                        color = ConstColours.ERROR_RED,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0x00000000)// 0xFF121212)
@Composable
fun GlassTextFieldPreview() {
    
    Column(Modifier.padding(16.dp)) {
        Text("Standard Registration:", color = Color.White)
        TextFieldRegistration(
            value = "",
            onValueChange = {},
            placeholder = "Введите текст..."
        )

        Spacer(Modifier.height(24.dp))

        Text("Glass Design (BasicTextField + TextFieldState):", color = Color.White)
        GlassTextField(
            value = "",
            onValueChange = {},
            placeholder = "Введите пароль...",
            isError = true
        )
    }
}
