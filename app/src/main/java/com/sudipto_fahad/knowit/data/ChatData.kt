package com.sudipto_fahad.knowit.data

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.ResponseStoppedException
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ChatData {
    private const val API_KEY = "AIzaSyBTyRbZtoenhkm65MTKq1u_pZlktAu1WQw"

    suspend fun getResponseWithImage(prompt: String): Chat {
        val generativeModel = GenerativeModel(
            modelName = "gemini-pro",
            apiKey = API_KEY
        )

        try {
            val response = withContext(Dispatchers.IO) {
                generativeModel.generateContent(prompt)
            }

            return Chat(
                prompt = response.text ?: "Error",
                bitmap = null,
                isFromUser = false
            )
        } catch (e: ResponseStoppedException) {
            return Chat(
                prompt = e.message ?: "Error",
                bitmap = null,
                isFromUser = false
            )
        }
    }



    suspend fun getResponseWithImage(prompt: String, bitmap: Bitmap): Chat {
        val generativeModel = GenerativeModel(
            modelName = "gemini-pro-vision",
            apiKey = API_KEY
        )

        try {

            val inputContent = content {
                image(bitmap)
                text(prompt)
            }

            val response = withContext(Dispatchers.IO) {
                generativeModel.generateContent(inputContent)
            }

            return Chat(
                prompt = response.text ?: "Error",
                bitmap = null,
                isFromUser = false
            )
        } catch (e: ResponseStoppedException) {
            return Chat(
                prompt = e.message ?: "Error",
                bitmap = null,
                isFromUser = false
            )
        }
    }
}