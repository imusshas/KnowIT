package com.sudipto_fahad.knowit

import android.graphics.Bitmap

sealed interface ChatUIEvent {
    data class UpdatePrompt(val newPrompt: String) : ChatUIEvent
    data class SendPrompt(
        val prompt: String,
        val bitmap: Bitmap?) : ChatUIEvent
}