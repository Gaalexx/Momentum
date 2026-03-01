package com.project.momentum.ui.screens.posts

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

data class PostData(
    val url: String,
    val name: String,
    val date: String,
    val description: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as PostData
        return url == other.url
    }

    override fun hashCode(): Int {
        return url.hashCode()
    }
}


@Serializable
data class PostItem(
    val name: String,
    val date: String,
    val description: String
)

fun readRandomEvent(context: Context): PostItem {
    val text = context.assets.open("posts.json")
        .bufferedReader()
        .use { it.readText() }
    val json = Json { ignoreUnknownKeys = true }
    val list = json.decodeFromString<List<PostItem>>(text)
    return list.random()
}

abstract class BasePostViewModel : ViewModel() {
    protected val _posts = mutableStateListOf<PostData>()
    val posts: List<PostData> get() = _posts

    protected var _selectedPost: PostData? by mutableStateOf(null)
    val selectedPost: PostData? get() = _selectedPost

    abstract fun addPhoto(context: Context, url: String)

    fun addRandomPhoto(context: Context) {
        addPhoto(context, "https://picsum.photos/300/300?random=${System.currentTimeMillis()}")
    }

    fun selectPost(post: PostData) {
        _selectedPost = post
    }

    fun clear() {
        _posts.clear()
        _selectedPost = null
    }

    // Общий метод для получения данных из JSON
    protected fun readRandomEvent(context: Context): PostItem {
        return com.project.momentum.ui.screens.posts.readRandomEvent(context)
    }
}