import cn.transfur.furbot.util.buildSignString
import cn.transfur.furbot.data.FurryPic
import cn.transfur.furbot.data.FurryPicServerResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.request.parameter
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

private val serializer = FurryPicServerResponse.serializer(FurryPic.serializer())

suspend fun main() {
    val response = HttpClient(OkHttp).use {
        val timestamp = System.currentTimeMillis() / 1000L
        val apiPath = "api/v2/getFursuitByID"
        it.get<String> {
            url("https://api.tail.icu/$apiPath")
            parameter("qq", System.getenv("qq"))
            parameter("timestamp", timestamp)
            parameter("sign", buildSignString(apiPath, timestamp, System.getenv("authKey")))
            parameter("fid", "3004")
        }
    }
    println(json.decodeFromString(serializer, response))
}