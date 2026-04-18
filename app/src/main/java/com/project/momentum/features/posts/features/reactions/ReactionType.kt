package com.project.momentum.features.posts.features.reactions

enum class ReactionType(val emoji: String, val serverKey: String) {
    LIKE("\uD83D\uDC4D", "like"),
    HEART("❤\uFE0F", "heart"),
    FIRE("\uD83D\uDD25", "fire"),
    CLOWN("\uD83E\uDD21", "clown"),
    HANDSHAKE("\uD83E\uDD1D", "handshake"),
    LOUDLYCRYING("\uD83D\uDE2D", "loudly_crying"),
    TRASH("\uD83E\uDD2E", "trash"),
    POOP("\uD83D\uDCA9", "poop"),
    SAD("☹\uFE0F", "sad"),
    SMILE("\uD83D\uDE42", "smile"),
    LAUGH("\uD83E\uDD23", "laugh"),
    CHECK_MARK("✅", "check_mark");
}
