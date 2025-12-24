import androidx.camera.core.CameraSelector
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentDataType.Companion.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.momentum.ConstColours
import com.project.momentum.BigCircleForMainScreenAction
import com.project.momentum.CaptionBasicInput
import com.project.momentum.FriendsPillButton
import com.project.momentum.ProfileCircleButton
import com.project.momentum.SendPhotoScreen
import com.project.momentum.SettingsCircleButton
import com.project.momentum.deleteByUri
import com.project.momentum.rememberCameraPermissionState
import com.project.momentum.CaptionBasicLabel
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.theme.AppTypography


@Composable
fun ProfileLabel(
    modifier: Modifier = Modifier,
    name: String,
    imageUrl: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(top = 7.dp).fillMaxWidth().size(70.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(67.dp)
                .clip(CircleShape)
        )

        Spacer(Modifier.width(13.dp))

        Text(
            text = name,
            color = Color.White,
            overflow = TextOverflow.Ellipsis,
            style = AppTextStyles.MainText
        )
    }
}

@Composable
fun ReactToPhoto(
    modifier: Modifier = Modifier,
    onReact: () -> Unit
    ){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(bottom = 53.dp,start = 26.dp,end = 26.dp, top = 7.dp)
            .clip(shape = RoundedCornerShape(25.dp))
            .fillMaxWidth()
            .background(
                color = ConstColours.MAIN_BACK_GRAY,
                shape = RoundedCornerShape(25.dp)
            )
            .padding(vertical = 7.dp,horizontal = 17.dp,)
    ){
        Text("Написать сообщение...",
            color = ConstColours.WHITE,
            style = AppTextStyles.MainText
        )
        Column(
            modifier = Modifier
                .weight(1f)
        ){}
    }
}


@Composable
fun WatchPhotoScreen(
    previewPainter: Painter? = null,
    modifier: Modifier = Modifier,
    onGoToTakePhoto: () -> Unit,
    onGoToGallery: () -> Unit,
    url: String?
) {
    val bg = ConstColours.BLACK
    val chrome2 = ConstColours.MAIN_BACK_GRAY
    val iconTint = ConstColours.WHITE

    val context = LocalContext.current
    var caption by rememberSaveable { mutableStateOf("") }
    val captionFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    var torchEnabled by remember { mutableStateOf(false) }

    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bg)
            .windowInsetsPadding(WindowInsets.systemBars),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileCircleButton(onClick = {}, backgroundColor = chrome2)
            Spacer(Modifier.weight(1f))
            FriendsPillButton(onClick = {})
            Spacer(Modifier.weight(1f))

            IconButton(
                onClick = { },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(chrome2)
                    .border(1.dp, ConstColours.MAIN_BACK_GRAY, CircleShape)
            ) { SettingsCircleButton(onClick = {}, backgroundColor = chrome2) }
        }

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.98f)
                .aspectRatio(1.10f)
                .clip(RoundedCornerShape(28.dp))
                .background(ConstColours.MAIN_BACK_GRAY)
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                CaptionBasicLabel("Текст", modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .focusRequester(captionFocusRequester))
            }
            //

        }
        Spacer(Modifier.height(5.dp))
        Text(modifier = Modifier.align(Alignment.CenterHorizontally), text = "01.01.2026", color = ConstColours.WHITE, style = AppTextStyles.SupportingText)
        ProfileLabel(name = "UserName", imageUrl = "https://cataas.com/cat")

        ReactToPhoto(onReact = {})

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    onGoToGallery()
                }
                    , modifier = Modifier.size(50.dp)) {
                    Icon(Icons.Default.Photo, modifier = Modifier.size(40.dp), contentDescription = "Flash", tint = iconTint)
                }

                Spacer(Modifier.weight(1f))
                BigCircleForMainScreenAction(
                    onClick = onGoToTakePhoto
                )
                Spacer(Modifier.weight(1f))

                IconButton(
                    onClick = {
                        captionFocusRequester.requestFocus()
                        keyboardController?.show()
                    },
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(Icons.Outlined.MoreHoriz, modifier = Modifier.size(40.dp), contentDescription = "Flip camera", tint = iconTint)
                }
            }

        }

        Spacer(Modifier.height(15.dp))

        Icon(
            imageVector = Icons.Outlined.KeyboardArrowDown,
            contentDescription = "More",
            tint = iconTint.copy(alpha = 0.9f),
            modifier = Modifier.size(34.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0C0F)
@Composable
private fun WatchPhotoScreenPreview() {
    MaterialTheme {
        WatchPhotoScreen(previewPainter = null, onGoToTakePhoto = {}, onGoToGallery = {}, url = null)
    }
}
