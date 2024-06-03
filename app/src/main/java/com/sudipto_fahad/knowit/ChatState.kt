package com.sudipto_fahad.knowit

import android.graphics.Bitmap
import com.sudipto_fahad.knowit.data.Chat

data class ChatState(
    val chatList: MutableList<Chat> = mutableListOf(),
    val prompt: String = "",
    val bitmap: Bitmap? = null
)
