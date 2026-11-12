package com.project.momentum.presentation.settings.models


sealed class SettingsError : Exception() {
    class Network : SettingsError()
    class Unauthorized : SettingsError()
    class Server(val serverMessage: String?) : SettingsError()
    class Unknown : SettingsError()
}
