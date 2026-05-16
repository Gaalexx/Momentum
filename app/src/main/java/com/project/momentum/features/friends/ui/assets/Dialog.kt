package com.project.momentum.features.friends.ui.assets

import android.view.Surface
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.PersonRemove
import androidx.compose.material.icons.outlined.RestoreFromTrash
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.project.momentum.R
import com.project.momentum.features.friends.ui.User
import com.project.momentum.features.friends.viewmodel.FriendsScreenEvent
import com.project.momentum.features.friends.viewmodel.SelectedIndex
import com.project.momentum.ui.assets.CancelButtonAdaptive
import com.project.momentum.ui.assets.ContinueButtonAdaptive
import com.project.momentum.ui.assets.TextFieldRegistration
import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.theme.ConstColours



@Composable
fun DeleteFriendDialog(
    friendToDelete: User,
    onAccept: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight(0.2f),
        shape = RoundedCornerShape(15.dp),
        color = ConstColours.MAIN_BACK_GRAY
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 5.dp, end = 5.dp)
                        ,
                    text = stringResource(
                        R.string.do_you_want_to_delete_a_friend,
                        friendToDelete.name
                    ),
                    style = AppTextStyles.SubHeadlines,
                    color = ConstColours.WHITE,
                    textAlign = TextAlign.Center
                )
            }

            Row(
                modifier = Modifier
                    .weight(0.75f)
                    .align(Alignment.CenterHorizontally)
                    .padding(5.dp)
            ) {
                ContinueButtonAdaptive(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .padding(2.dp),
                    onClick = {onAccept()},
                    backGroundColor = ConstColours.RED,
                    text = stringResource(R.string.delete)
                )
                CancelButtonAdaptive(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .padding(2.dp),
                    onClick = {onDismiss()},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ConstColours.TRANSPARENT_WHITE_ALPHA0,
                        contentColor = ConstColours.WHITE
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PreviewDeleteFriendDialog() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        DeleteFriendDialog(
            User("123", "Друк", "1231231")
        )
    }
}
