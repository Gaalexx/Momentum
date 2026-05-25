package com.project.momentum.ui.theme

import com.project.momentum.data.auth.datastore.ThemeDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeManager @Inject constructor(
    private val themeDataStore: ThemeDataStore
) {
    private val _isDefaultTheme = MutableStateFlow(true)
    val isDefaultTheme = _isDefaultTheme.asStateFlow()

    init {
        themeDataStore.isDefaultThemeFlow
            .onEach { isDefault ->
                _isDefaultTheme.value = isDefault
            }
            .launchIn(CoroutineScope(Dispatchers.IO))
    }

    fun setDefaultTheme(enabled: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            themeDataStore.setDefaultTheme(enabled)
        }
        //_isDefaultTheme.value = enabled
    }

    fun toggleTheme() {
        setDefaultTheme(!_isDefaultTheme.value)
    }
}