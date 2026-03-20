package com.project.momentum.ui.assets

import androidx.appcompat.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.theme.ConstColours

@Composable
fun AddFriendPage(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {

    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.4f)
            .clip(RoundedCornerShape(20.dp))
            .background(ConstColours.MAIN_BACK_GRAY)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ){
            Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.2f), contentAlignment = Alignment.Center){
                Text(
                    text = stringResource(com.project.momentum.R.string.friend_search),
                    color = ConstColours.WHITE,
                    style = AppTextStyles.SupportingText
                )
            }
            Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.2f), contentAlignment = Alignment.Center){
                TextFieldRegistration(
                    value = value,
                    onValueChange = onValueChange,
                    isError = isError,
                    modifier = Modifier.fillMaxWidth(0.8f).fillMaxHeight(),
                    keyboardOptions = keyboardOptions,
                )
            }


        }

    }

}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PreviewPager() {
    AddFriendPage("asd", {})
}