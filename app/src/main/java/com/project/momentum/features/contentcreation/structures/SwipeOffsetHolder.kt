package com.project.momentum.features.contentcreation.structures

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
class SwipeOffsetHolder(
    val bubbleSize: Dp
) {
    val buttonTopOffset = mutableStateOf(Offset.Zero)
    var floatingProgress = Animatable(0f)
    val offsetYDif = mutableIntStateOf(0)
    val maximumBubbleYOffset = mutableIntStateOf(0)
    val isLocked = mutableStateOf(false)

    fun refresh() {
        isLocked.value = false
        floatingProgress = Animatable(0f)
        offsetYDif.intValue = 0
    }

    companion object {
        val Saver = mapSaver(
            save = {
                mapOf(
                    "bubble" to it.bubbleSize.value,
                    "diff" to it.offsetYDif.intValue,
                    "floatingProgress" to it.floatingProgress.value,
                    "buttonTopOffsetX" to it.buttonTopOffset.value.x,
                    "buttonTopOffsetY" to it.buttonTopOffset.value.y,
                    "maximumBubbleYOffset" to it.maximumBubbleYOffset,
                    "isLocked" to if (it.isLocked.value) 1 else 0
                )
            },
            restore = { map ->
                SwipeOffsetHolder((map["bubble"] as? Float ?: 0f).dp).apply {
                    offsetYDif.intValue = map["diff"] as? Int ?: 0
                    floatingProgress = Animatable(map["floatingProgress"] as? Float ?: 0f)
                    buttonTopOffset.value = Offset(
                        x = map["buttonTopOffsetX"] as? Float ?: 0f,
                        y = map["buttonTopOffsetY"] as? Float ?: 0f
                    )
                    maximumBubbleYOffset.intValue = map["maximumBubbleYOffset"] as? Int ?: 0
                    isLocked.value = (map["isLocked"] as? Int ?: 0) == 1
                }
            }
        )
    }
}

@Composable
fun rememberSwipeOffsetHolder(bubbleSize: Dp): SwipeOffsetHolder =
    rememberSaveable(saver = SwipeOffsetHolder.Saver) { SwipeOffsetHolder(bubbleSize) }