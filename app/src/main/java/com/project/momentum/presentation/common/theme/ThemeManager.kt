package com.project.momentum.presentation.common.theme

import com.project.momentum.di.ApplicationScope
import com.project.momentum.domain.settings.repository.ThemeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeManager @Inject constructor(
    private val themeRepository: ThemeRepository,
    @ApplicationScope private val applicationScope: CoroutineScope,
) {
    val isDefaultTheme = themeRepository.isDefaultTheme.stateIn(
        applicationScope,
        SharingStarted.Eagerly,
        true,
    )

    fun setDefaultTheme(enabled: Boolean) {
        applicationScope.launch {
            themeRepository.setDefaultTheme(enabled)
        }
    }

    fun toggleTheme() {
        setDefaultTheme(!isDefaultTheme.value)
    }
}
