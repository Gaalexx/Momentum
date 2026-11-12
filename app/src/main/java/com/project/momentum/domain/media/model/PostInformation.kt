package com.project.momentum.domain.media.model

/** An opaque local URI; platform-specific URI handling belongs to the data layer. */
data class PostInformation(
    val uri: String,
    val mimeType: String,
    val mediaType: MediaType,
    val size: Long,
    val durationMs: Long? = null,
    val label: String? = null,
    val receiverIds: List<String> = emptyList()
)
