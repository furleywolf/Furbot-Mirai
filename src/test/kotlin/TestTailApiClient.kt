import cn.transfur.furbot.data.Fids
import cn.transfur.furbot.util.buildSignString
import cn.transfur.furbot.data.TailApiServerResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.request.parameter
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

suspend fun main() {
    val response = HttpClient(OkHttp).use { client ->
        val timestamp = System.currentTimeMillis() / 1000L
        val apiPath = "api/v2/getFursuitFid"
        try {
            client.get<String> {
                url("https://api.tail.icu/$apiPath")
                parameter("qq", System.getenv("qq"))
                parameter("timestamp", timestamp)
                parameter("sign", buildSignString(apiPath, timestamp, System.getenv("authKey")))
                parameter("name", "阿巴")
            }
        } catch (e: ClientRequestException) {
            println(e.response.status)
            null
        }
    } ?: return
    val serializer = TailApiServerResponse.serializer(Fids.serializer())
    println(json.decodeFromString(serializer, response))
}