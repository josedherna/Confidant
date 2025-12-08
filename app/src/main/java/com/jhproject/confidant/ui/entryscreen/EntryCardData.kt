package com.jhproject.confidant.ui.entryscreen

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.jhproject.confidant.R
import com.jhproject.confidant.ui.theme.AwfulDark
import com.jhproject.confidant.ui.theme.AwfulLight
import com.jhproject.confidant.ui.theme.BadDark
import com.jhproject.confidant.ui.theme.BadLight
import com.jhproject.confidant.ui.theme.GoodDark
import com.jhproject.confidant.ui.theme.GoodLight
import com.jhproject.confidant.ui.theme.GreatDark
import com.jhproject.confidant.ui.theme.GreatLight
import com.jhproject.confidant.ui.theme.MehDark
import com.jhproject.confidant.ui.theme.MehLight

@Immutable
data class EntryCardData(
    val id: Int,
    val date: String,
    val time: String,
    val note: String,
    val mood: String
)


enum class Mood(
    val key: String,
    val label: Int,
    val icon: Int,
    val iconFilled: Int,
    val lightColor: Color,
    val darkColor: Color
) {
    AWFUL(
        "awful",
        R.string.awful,
        R.drawable.sentiment_sad_24px,
        R.drawable.sentiment_sad_filled_24px,
        AwfulLight,
        AwfulDark),
    BAD(
        "bad",
        R.string.bad,
        R.drawable.sentiment_dissatisfied_24px,
        R.drawable.sentiment_dissatisfied_filled_24px,
        BadLight,
        BadDark
    ),
    MEH(
        "meh",
        R.string.meh,
        R.drawable.sentiment_neutral_24px,
        R.drawable.sentiment_neutral_filled_24px,
        MehLight,
        MehDark),
    GOOD(
        "good",
        R.string.good,
        R.drawable.sentiment_satisfied_24px,
        R.drawable.sentiment_satisfied_filled_24px,
        GoodLight,
        GoodDark
    ),
    GREAT(
        "great",
        R.string.great,
        R.drawable.sentiment_excited_24px,
        R.drawable.sentiment_excited_filled_24px,
        GreatLight,
        GreatDark
    );

    companion object {
        private val lookup: Map<String, Mood> =
            entries.associateBy { it.key }

        fun fromKey(key: String): Mood =
            lookup[key] ?: MEH
    }
}
