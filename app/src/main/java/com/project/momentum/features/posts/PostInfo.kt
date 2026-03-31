package com.project.momentum.features.posts

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.project.momentum.features.account.models.PostData


@Serializable
data class PostItem(
    val name: String,
    val date: String,
    val description: String
)