package com.project.momentum.data.registration

sealed class RegistrationNavEvent {
    object NavigateToNextScreen: RegistrationNavEvent()
    object NavigateToPrevScreen: RegistrationNavEvent()
}