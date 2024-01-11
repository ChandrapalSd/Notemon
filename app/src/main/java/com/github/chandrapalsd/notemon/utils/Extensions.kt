package com.github.chandrapalsd.notemon.utils

import android.content.Intent
import android.os.Build
import com.github.chandrapalsd.notemon.models.Note

fun Intent.getParcelableNoteExtra(name: String): Note? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableExtra(name, Note::class.java)
    } else {
        @Suppress("DEPRECATION")
        getParcelableExtra<Note>(name)
    }
}
