package com.project.momentum

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

data class PostData(
    val url: String,
    val name: String,
    val date: String,
    val description: String
) {}


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
    println("1")
    val json = Json { ignoreUnknownKeys = true }
    println("2")
    try {
        val list = json.decodeFromString<List<PostItem>>(text)
        return list.random()
    } catch (e: Exception) {
        e.printStackTrace()
        throw e
    }
    println("3")
    return PostItem("asasd", "asdas", "asdasd")//list.random()
}