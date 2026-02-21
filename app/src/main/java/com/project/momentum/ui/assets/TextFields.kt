package com.project.momentum.ui.assets

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.momentum.ConstColours
import com.project.momentum.ui.theme.AppTextStyles

@Composable
fun TextFieldRegistration(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth(),
        textStyle = AppTextStyles.MainText.copy(
            textAlign = TextAlign.Center
        ),
        isError = isError,
        keyboardOptions = keyboardOptions,
        maxLines = 1,
        shape = RoundedCornerShape(50.dp),
        colors = TextFieldDefaults.textFieldColors(
            disabledTextColor = ConstColours.WHITE,
            focusedLabelColor = ConstColours.WHITE,
            unfocusedLabelColor = ConstColours.WHITE,
            backgroundColor = ConstColours.WHITE
        )
    )
}

@Preview(showBackground = false)
@Composable
fun TextFieldRegistrationPreview() {
    TextFieldRegistration(
        value = "vjmievnjebnvj",
        onValueChange = {}
    )
}