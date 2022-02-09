package cn.transfur.furbot.network

import cn.transfur.furbot.Config
import cn.transfur.furbot.data.TailApiServerResponse
import cn.transfur.furbot.util.buildSignString
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.request.parameter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

object TailApiClient {
    private const val TAIL_API_HOST: String = "https://api.tail.icu/"

    private lateinit var client: HttpClient

    private val json: Json = Json {
        ignoreUnknownKeys = true
    }

    suspend fun <T> getFromTailApi(
        dataSerializer: KSerializer<T>,
        apiPath: String,
        vararg extraParameters: Pair<String, Any?>
    ): T? {
        val timestamp = System.currentTimeMillis() / 1000L
        val parameters = mapOf(
            "qq" to Config.furbot.qq,
            "timestamp" to timestamp,
            "sign" to buildSignString(apiPath, timestamp, Config.furbot.authKey),
            *extraParameters
        )

        return try {
            val rawResponse = client.get<String> {
                url(TAIL_API_HOST + apiPath)
                parameters.forEach(::parameter)
            }
            json.decodeFromString(
                deserializer = TailApiServerResponse.serializer(dataSerializer),
                string = rawResponse
            ).data
        } catch (e: ClientRequestException) {
            null
        }
    }

    suspend inline fun <reified T> getFromTailApi(
        apiPath: String,
        vararg extraParameters: Pair<String, Any?>
    ): T? = getFromTailApi(serializer(), apiPath, *extraParameters)

    suspend fun getImageBytes(urlString: String): ByteArray {
        return client.get(urlString)
    }

    fun open() {
        client = HttpClient(OkHttp)
    }

    fun close() {
        client.close()
    }
}