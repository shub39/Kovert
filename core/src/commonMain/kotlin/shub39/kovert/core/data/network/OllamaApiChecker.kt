package shub39.kovert.core.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json

object OllamaApiChecker {
    val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json()
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println(message)
                }
            }
        }
    }

    suspend fun isUrlValid(url: String): Boolean {
        return try {
            val response = client.get(url).body<String>()
            response == "Ollama is running"
        } catch (_: Exception) {
            false
        }
    }
}