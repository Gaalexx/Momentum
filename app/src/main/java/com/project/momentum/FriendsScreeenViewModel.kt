package com.project.momentum

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import org.json.JSONArray
import org.json.JSONObject

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext

    private val _users = mutableStateOf<List<User>>(emptyList())
    val users: State<List<User>> = _users

    private val _userFriends = mutableStateOf<List<User>>(emptyList())
    val userFriends: State<List<User>> = _userFriends

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        loadUsersFromJson()
    }

    private fun loadUsersFromJson() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val jsonString = loadJsonFromAssets("users.json")
                val usersList = parseUsersFromJson(jsonString)
                _users.value = usersList
            } catch (e: Exception) {
                e.printStackTrace()
                _users.value = createMockUsers()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadFriendsForUser(user: User) {
        viewModelScope.launch {
            val friendsIds = user.friends.map { it.id }
            val filteredFriends = _users.value.filter { it.id in friendsIds }
            if (filteredFriends.isNotEmpty())
            {
                _userFriends.value = filteredFriends
            } else {
                _userFriends.value = createMockFriends(user)
            }
        }
    }

    // Загрузка JSON из assets
    private suspend fun loadJsonFromAssets(fileName: String): String {
        return withContext(Dispatchers.IO) {
            try {
                context.assets.open(fileName).bufferedReader().use { it.readText() }
            } catch (e: Exception) {
                ""
            }
        }
    }

    // Парсинг JSON с использованием org.json
    private fun parseUsersFromJson(jsonString: String): List<User> {
        val usersList = mutableListOf<User>()

        if (jsonString.isEmpty()) {
            return usersList
        }

        try {
            val jsonArray = JSONArray(jsonString)

            for (i in 0 until jsonArray.length()) {
                val userJson = jsonArray.getJSONObject(i)
                val user = parseUserFromJson(userJson)
                usersList.add(user)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return usersList
    }

    // Парсинг одного пользователя
    private fun parseUserFromJson(jsonObject: JSONObject): User {
        return User(
            id = jsonObject.getString("id"),
            name = jsonObject.getString("name"),
            avatarUrl = jsonObject.getString("avatarUrl"),
            isOnline = jsonObject.optBoolean("isOnline", false),
            description = jsonObject.optString("description", null).takeIf { it != "null" && it.isNotEmpty() },
            friends = parseFriendsFromJson(jsonObject.optJSONArray("friends"))
        )
    }

    private fun parseFriendsFromJson(friendsArray: JSONArray?): List<Friend> {
        val friendsList = mutableListOf<Friend>()

        if (friendsArray == null) {
            return friendsList
        }

        try {
            for (i in 0 until friendsArray.length()) {
                val friendJson = friendsArray.getJSONObject(i)
                val friend = Friend(
                    id = friendJson.getString("id"),
                    name = friendJson.getString("name")
                )
                friendsList.add(friend)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return friendsList
    }

    private fun createMockUsers(): List<User> {
        return listOf(
            User(
                id = "user_1",
                name = "Александр Иванов",
                avatarUrl = "https://example.com/avatar1.jpg",
                isOnline = true,
                description = "Веб-разработчик",
                friends = listOf(
                    Friend("user_2", "Мария Петрова"),
                    Friend("user_3", "Иван Сидоров")
                )
            ),
            User(
                id = "user_2",
                name = "Мария Петрова",
                avatarUrl = "https://example.com/avatar2.jpg",
                isOnline = false,
                description = "Дизайнер интерфейсов",
                friends = listOf(
                    Friend("user_1", "Александр Иванов"),
                    Friend("user_4", "Елена Смирнова")
                )
            ),
            User(
                id = "user_3",
                name = "Иван Сидоров",
                avatarUrl = "https://example.com/avatar3.jpg",
                isOnline = true,
                description = "Мобильный разработчик",
                friends = listOf(
                    Friend("user_1", "Александр Иванов"),
                    Friend("user_5", "Дмитрий Козлов")
                )
            ),
            User(
                id = "user_4",
                name = "Елена Смирнова",
                avatarUrl = "https://example.com/avatar4.jpg",
                isOnline = false,
                description = "Project Manager",
                friends = listOf(
                    Friend("user_2", "Мария Петрова"),
                    Friend("user_6", "Анна Кузнецова")
                )
            ),
            User(
                id = "user_5",
                name = "Дмитрий Козлов",
                avatarUrl = "https://example.com/avatar5.jpg",
                isOnline = true,
                description = "DevOps инженер",
                friends = listOf(
                    Friend("user_3", "Иван Сидоров"),
                    Friend("user_7", "Сергей Попов")
                )
            ),
            User(
                id = "user_6",
                name = "Анна Кузнецова",
                avatarUrl = "https://example.com/avatar6.jpg",
                isOnline = false,
                description = "QA инженер",
                friends = listOf(
                    Friend("user_4", "Елена Смирнова"),
                    Friend("user_8", "Ольга Васильева")
                )
            ),
            User(
                id = "user_7",
                name = "Сергей Попов",
                avatarUrl = "https://example.com/avatar7.jpg",
                isOnline = true,
                description = "Бэкенд разработчик",
                friends = listOf(
                    Friend("user_5", "Дмитрий Козлов"),
                    Friend("user_9", "Николай Федоров")
                )
            )
        )
    }

    private fun createMockFriends(user: User): List<User> {
        return user.friends.mapIndexed { index, friend ->
            User(
                id = friend.id,
                name = friend.name,
                avatarUrl = "https://picsum.photos/300/300?random=${index + 100}",
                isOnline = index % 3 == 0,
                description = if (index % 2 == 0) "Друг пользователя ${user.name}" else null,
                friends = emptyList()
            )
        }
    }
}