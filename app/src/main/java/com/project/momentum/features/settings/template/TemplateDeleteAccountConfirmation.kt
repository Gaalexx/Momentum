package com.project.momentum.features.settings.template

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.project.momentum.R
import com.project.momentum.ui.assets.ButtonForDelete
import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.theme.ConstColours

@Preview(showBackground = true)
@Composable
fun DeleteAccountConfirmationScreenPreview() {
    TemplateDeleteAccountConfirmation(
        onConfirm = {},
        onCancel = {}
    )
}

@Composable
fun TemplateDeleteAccountConfirmation(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ConstColours.BLACK.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(ConstColours.WHITE, shape = RoundedCornerShape(dimensionResource(R.dimen.medium_padding)))
                .padding(dimensionResource(R.dimen.delete_account_stroke))
                .width(dimensionResource(R.dimen.delete_account_padding_width)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.label_delete_account_confirmation),
                style = AppTextStyles.Headlines,
                color = ConstColours.BLACK,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.delete_account_stroke)))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Spacer(modifier = Modifier.weight(1f))
                ButtonForDelete(
                    onClick = onConfirm,
                    text = stringResource(R.string.button_delete_account_yes),
                    color =  ConstColours.ERROR_RED,
                )
                Spacer(modifier = Modifier.weight(2f))
                ButtonForDelete(
                    onClick = onCancel,
                    text = stringResource(R.string.button_delete_account_no),
                    color =  ConstColours.BLACK,
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
