package com.project.momentum.ui.screens.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SettingsMainScreenViewModel : ViewModel(){
    val _publicationsEnabled = mutableStateOf(true)
    val _reactionsEnabled = mutableStateOf(true)
    val _inAppNotifications = mutableStateOf(true)
    val _recommendToContacts = mutableStateOf(true)
    val _allowAddFromAnyone = mutableStateOf(true)
    val _confirmBeforePosting = mutableStateOf(true)


    var publicationsEnabled by _publicationsEnabled
    var reactionsEnabled by _reactionsEnabled
    var inAppNotifications by _inAppNotifications
    var recommendToContacts by _recommendToContacts
    var allowAddFromAnyone by _allowAddFromAnyone
    var confirmBeforePosting by _confirmBeforePosting
}