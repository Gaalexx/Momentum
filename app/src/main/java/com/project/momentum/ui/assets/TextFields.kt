package com.project.momentum.ui.assets

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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
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
        shape = RoundedCornerShape(36.dp),
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
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isError: Boolean = false,
    errorText: String? = null,
    textColor: Color = Color.White,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val accentColor = if (isError) ConstColours.ERROR_RED else textColor
    val currentBorderColor = if (isFocused) accentColor else accentColor.copy(alpha = 0.4f)

    val glassGradient = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.25f),
            Color.White.copy(alpha = 0.05f)
        )
    )

    BasicTextField(
        state = state,
        modifier = modifier.fillMaxWidth(),
        interactionSource = interactionSource,
        textStyle = AppTextStyles.MainText.copy(
            color = textColor,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = keyboardOptions,
        lineLimits = TextFieldLineLimits.SingleLine,
        cursorBrush = SolidColor(textColor),
        decorator = { innerTextField ->
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .shadow(
//                            elevation = if (isFocused) 12.dp else 6.dp,
//                            shape = RoundedCornerShape(36.dp),
//                            clip = false,
//                            spotColor = if (isFocused) accentColor.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.4f)
//                        )
                        .background(glassGradient, RoundedCornerShape(36.dp))
                        .drawBehind {
                            val cornerRadiusPx = 36.dp.toPx()
                            val radius = CornerRadius(cornerRadiusPx, cornerRadiusPx)

                            drawRoundRect(
                                brush = Brush.radialGradient(
                                    0.0f to textColor.copy(alpha = 0.08f),
                                    1.0f to Color.Transparent,
                                    center = center,
                                    radius = size.maxDimension
                                ),
                                cornerRadius = radius
                            )

                            drawRoundRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.4f),
                                        Color.Transparent
                                    ),
                                    startY = 0f,
                                    endY = size.height * 0.5f
                                ),
                                cornerRadius = radius,
                                style = Stroke(width = 1.5.dp.toPx())
                            )

                            drawRoundRect(
                                color = Color.Black.copy(alpha = 0.1f),
                                cornerRadius = radius,
                                style = Stroke(width = 1.dp.toPx()),
                                alpha = 0.5f
                            )
                        }
                        .border(
                            width = 1.dp,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    currentBorderColor.copy(alpha = 0.7f),
                                    currentBorderColor.copy(alpha = 0.1f)
                                )
                            ),
                            shape = RoundedCornerShape(36.dp)
                        )
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.text.isEmpty() && placeholder != null) {
                        Text(
                            text = placeholder,
                            style = AppTextStyles.MainText,
                            color = textColor.copy(alpha = 0.4f),
                            textAlign = TextAlign.Center
                        )
                    }

                    innerTextField()

                    if (isFocused) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth(0.4f)
                                .height(2.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color.Transparent, accentColor.copy(alpha = 0.8f), Color.Transparent)
                                    )
                                )
                        )
                    }
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

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun GlassTextFieldPreview() {
    val glassState = rememberTextFieldState()
    
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
            state = glassState,
            placeholder = "Введите пароль...",
            isError = false
        )
    }
}
