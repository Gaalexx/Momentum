package com.project.momentum.data.registration

sealed class NavEvent {
    object NavigateToNextScreen: NavEvent()
    object NavigateToNextSubScreen: NavEvent()
    object NavigateToPrevScreen: NavEvent()
}