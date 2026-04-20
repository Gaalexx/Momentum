package com.project.momentum.features.posts.features.reactions.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Reply
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Reply
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.project.momentum.R
import com.project.momentum.features.posts.features.reactions.ReactionData
import com.project.momentum.features.posts.features.reactions.ReactionType
import com.project.momentum.ui.assets.SubButton
import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.theme.ConstColours

@Composable
fun ReactionButton(
    emoji: String,
    size: Dp,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .height(size)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(50))
            .background(ConstColours.MAIN_BACK_GRAY)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji,
            style = AppTextStyles.MainText,
            color = ConstColours.WHITE,
        )
    }
}

@Composable
fun ReactionButtonWithCounter(
    emoji: ReactionType,
    counter: Int,
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
    avatarURL: String? = null,
    onClick: () -> Unit = {},
) {
    val bg = if (isActive) ConstColours.MAIN_BRAND_BLUE.copy(alpha = 0.3f)
            else ConstColours.MAIN_BACK_GRAY.copy(alpha = 0.5f)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 4.dp)
        ) {
            Spacer(Modifier.weight(1f))
            Text(
                text = emoji.emoji,
                style = AppTextStyles.MainText,
                color = ConstColours.WHITE,
            )
//            Spacer(Modifier.width(4.dp))
            Spacer(Modifier.weight(1f))
            if (counter == 1 && avatarURL != null) {
                AsyncImage(
                    model = avatarURL,
                    contentDescription = null,
                    modifier = Modifier
                        .height(20.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(50))
                )
            }
            else {
                Text(
                    text = counter.toString(),
                    style = AppTextStyles.MainText,
                    color = ConstColours.WHITE,
                )
            }
            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
fun DialogReactions() {
    Column(
//        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
//        LazyRow(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clip(RoundedCornerShape(50))
//                .background(ConstColours.MAIN_BACK_GRAY)
//                .padding(horizontal = 4.dp),
////                    horizontalArrangement = Arrangement.spacedBy(4.dp),
//            userScrollEnabled = true,
//        ) {
//            items(
//                items = ReactionType.entries,
//                key = { it.serverKey }
//            ) {
//                Box(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(50))
//                ) {
//                    Text(
//                        text = it.emoji,
//                        color = ConstColours.WHITE,
//                        style = AppTextStyles.MainText,
//                        modifier = Modifier.padding(8.dp)
//                    )
//                }
//            }
//        }

        BoxWithConstraints(
            modifier = Modifier
//                .fillMaxWidth()
                .clip(RoundedCornerShape(50))
                .background(ConstColours.MAIN_BACK_GRAY)
        ) {
            val availableWidth = maxWidth - 16.dp
            val itemApproxWidth = with(LocalDensity.current) { 20.sp.toDp() } + 16.dp
            val maxVisibleItems = (availableWidth / itemApproxWidth).toInt()

            val reactions = ReactionType.entries
            Row(
                Modifier.padding(horizontal = 8.dp)
            ) {
                if (reactions.size > maxVisibleItems) {
                    reactions.take(maxVisibleItems - 1).forEach { reaction ->
                        ReactionButton(
                            emoji = reaction.emoji,
                            size = itemApproxWidth
                        )
                    }
                    Box(
                        modifier = Modifier
                            .width(itemApproxWidth)
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = null,
                            tint = ConstColours.WHITE.copy(alpha = 0.6f)
                        )
                    }
                } else {
                    reactions.forEach { reaction ->
                        ReactionButton(
                            emoji = reaction.emoji,
                            size = itemApproxWidth
                        )
                    }

                }
            }
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10))
                .background(ConstColours.MAIN_BACK_GRAY)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                EventButton(
                    text = R.string.template_sub_button,
                    icon = Icons.Outlined.Reply,
                    onClick = {}
                )
                EventButton(
                    text = R.string.template_sub_button,
                    icon = Icons.Outlined.ContentCopy,
                    onClick = {}
                )
                EventButton(
                    text = R.string.template_sub_button,
                    icon = Icons.Outlined.Delete,
                    onClick = {}
                )
            }
        }
    }
}

@Composable
fun EventButton(
    @StringRes text: Int,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ConstColours.WHITE
        )
        SubButton(
            text = text,
            onClick = onClick
        )
    }
}

@Composable
fun ReactionsGrid(
    postOwner: String,
    reactionsData: List<ReactionData>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid( //TODO: обработать вариант когда реакций больше чем 3 ряда
        columns = GridCells.Adaptive(60.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        items(
            items = reactionsData.sortedByDescending(ReactionData::count),
            key = { it.emoji.serverKey }
        ) {
            ReactionButtonWithCounter(
                emoji = it.emoji,
                counter = it.count,
                isActive = postOwner in it.users
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0C0F)
@Composable
private fun DialogReactionsPreview() {
    MaterialTheme {
        DialogReactions()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0C0F)
@Composable
private fun ReactionButtonPreview() {
    MaterialTheme {
        Row {
            ReactionButton(
                emoji = ReactionType.LIKE.emoji,
                size = 40.dp
            )
            ReactionButtonWithCounter(
                emoji = ReactionType.LIKE,
                counter = 4,
                modifier = Modifier.width(60.dp)
            )
            ReactionButtonWithCounter(
                emoji = ReactionType.LIKE,
                counter = 5,
                isActive = true,
                modifier = Modifier.width(60.dp)
            )
        }
    }
}