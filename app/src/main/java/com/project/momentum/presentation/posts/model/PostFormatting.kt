package com.project.momentum.presentation.posts.model

import com.project.momentum.domain.posts.model.PostData
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun PostData.getDate(): String = createdAtInstantOrNull()
    ?.atZone(ZoneId.systemDefault())
    ?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
    .orEmpty()
