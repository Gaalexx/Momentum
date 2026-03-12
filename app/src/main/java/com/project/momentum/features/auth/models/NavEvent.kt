package com.project.momentum.features.auth.models

sealed class NavEvent {
    object NavigateToNextScreen : NavEvent()
    object NavigateToNextSubScreen : NavEvent()
    object NavigateToPrevScreen : NavEvent()
}