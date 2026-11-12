package com.project.momentum.presentation.auth.models

sealed class NavEvent {
    object NavigateToNextScreen : NavEvent()
    object NavigateToNextSubScreen : NavEvent()
    object NavigateToPrevScreen : NavEvent()
}