package cn.transfur.furbot.network

import cn.transfur.furbot.Config
import cn.transfur.furbot.buildSignString
import cn.transfur.furbot.data.FurryPic
import cn.transfur.furbot.data.FurryPicServerResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.*
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.request.parameter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

object FurryPicClient {
    private const val API_HOST: String = "https://api.tail.icu/"

    private lateinit var client: HttpClient

    private val json: Json = Json {
        ignoreUnknownKeys = true
    }

    private val serializer: KSerializer<FurryPicServerResponse<FurryPic>>
        = FurryPicServerResponse.serializer(FurryPic.serializer())

    suspend fun doGetFur(
        apiPath: String,
        vararg extraParameters: Pair<String, String>
    ): FurryPic? {
        val timestamp = System.currentTimeMillis() / 1000L
        val parameters = mapOf(
            "qq" to Config.qq,
            "timestamp" to timestamp,
            "sign" to buildSignString(apiPath, timestamp, Config.authKey),
            *extraParameters
        )

        return try {
            val response = client.get<String> {
                url(API_HOST + apiPath)
                parameters.forEach(::parameter)
            }
            json.decodeFromString(serializer, response).data
        } catch (e: ClientRequestException) {
            null
        }
    }

    suspend fun doGetImageBytes(urlString: String): ByteArray {
        return client.get(urlString)
    }

    fun open() {
        client = HttpClient(OkHttp)
    }

    fun close() {
        client.close()
    }
}