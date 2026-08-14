package com.project.momentum.features.contentcreation.structures

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset

@Stable
class SwipeOffsetHolder {

    var dragOffset by mutableStateOf(Offset.Zero)
        private set

    var buttonsTopOffset by mutableStateOf(Offset.Zero)
        private set

    fun updateDragOffset(offset: Offset) {
        dragOffset = offset
    }

    fun addDragOffset(offset: Offset) {
        dragOffset += offset
    }

    fun updateButtonsTopOffset(offset: Offset) {
        buttonsTopOffset = offset
    }

    fun resetDrag() {
        dragOffset = Offset.Zero
    }
}