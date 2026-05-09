package com.project.momentum.features.settings.models

import androidx.compose.ui.res.stringResource
import com.project.momentum.R

sealed class SettingsError : Exception() {
    class Network : SettingsError()
    class Unauthorized : SettingsError()
    class Server(val serverMessage: String?) : SettingsError()
    class Unknown : SettingsError()
}
