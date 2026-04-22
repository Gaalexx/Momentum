package com.project.momentum.features.posts.features.reactions.models

enum class ReactionType(val emoji: String, val serverKey: String) {
    LIKE("\uD83D\uDC4D", "like"),
    HEART("❤\uFE0F", "heart"),
    FIRE("\uD83D\uDD25", "fire"),
    CLOWN("\uD83E\uDD21", "clown"),
    HANDSHAKE("\uD83E\uDD1D", "handshake"),
    POOP("\uD83D\uDCA9", "poop"),
    CHECK_MARK("✅", "check_mark"),

    GRINNING_FACE("\uD83D\uDE00", "grinning_face"),
    GRINNING_FACE_WITH_BIG_EYES("\uD83D\uDE03", "grinning_face_with_big_eyes"),
    GRINNING_FACE_WITH_SMILING_EYES("\uD83D\uDE04", "grinning_face_with_smiling_eyes"),
    BEAMING_FACE_WITH_SMILING_EYES("\uD83D\uDE01", "beaming_face_with_smiling_eyes"),
    GRINNING_SQUINTING_FACE("\uD83D\uDE06", "grinning_squinting_face"),
    GRINNING_FACE_WITH_SWEAT("\uD83D\uDE05", "grinning_face_with_sweat"),
    ROLLING_ON_THE_FLOOR_LAUGHING_FACE("\uD83E\uDD23", "rolling_on_the_floor_laughing_face"),
    FACE_WITH_TEARS_OF_JOY("\uD83D\uDE02", "face_with_tears_of_joy"),
    SLIGHTLY_SMILING_FACE("\uD83D\uDE42", "slightly_smiling_face"),
    WINKING_FACE("\uD83D\uDE09", "winking_face"),
    SMILING_FACE_WITH_SMILING_EYES("\uD83D\uDE0A", "smiling_face_with_smiling_eyes"),
    SMILING_FACE_WITH_HALO("\uD83D\uDE07", "smiling_face_with_halo"),
    SMILING_FACE_WITH_HEARTS("\uD83E\uDD70", "smiling_face_with_hearts"),
    SMILING_FACE_WITH_HEART_EYES("\uD83D\uDE0D", "smiling_face_with_heart_eyes"),
    STAR_STRUCK("\uD83E\uDD29", "star_struck"),
    FACE_BLOWING_KISS("\uD83D\uDE18", "face_blowing_kiss"),
    KISSING_FACE("\uD83D\uDE17", "kissing_face"),
    SMILING_FACE("☺\uFE0F", "smiling_face"),
    KISSING_FACE_WITH_CLOSED_EYES("\uD83D\uDE1A", "kissing_face_with_closed_eyes"),
    KISSING_FACE_WITH_TEAR("\uD83E\uDD72", "kissing_face_with_tear"),
    SMIRKING_FACE("\uD83D\uDE0F", "smirking_face"),

    LOUDLY_CRYING("\uD83D\uDE2D", "loudly_crying"),
    TRASH("\uD83E\uDD2E", "trash"),
    SAD("☹\uFE0F", "sad"),
    ;
}