package rx.music.net.interceptors


import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import rx.music.net.BaseFields.Companion.APP_LOG
import rx.music.net.BaseFields.Companion.GCM_API
import java.io.IOException

/** Created by Maksim Sukhotski on 5/15/2017. */

class LoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.i("LoggingInterceptor", "inside intercept callback")
        val request = chain.request()
        val response = chain.proceed(request)
        val bodyString = response.body().string()
        if (request.url().toString() == GCM_API) {
            val t1 = System.nanoTime()
            var requestLog = String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers())
            if (request.method().compareTo("post", ignoreCase = true) == 0) {
                requestLog = "\n" + requestLog + "\n" + bodyToString(request)
            }
            Log.d(APP_LOG, "request" + "\n" + requestLog)
            val t2 = System.nanoTime()
            val responseLog = String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6, response.headers())
            Log.d(APP_LOG, "response only" + "\n" + bodyString)
            Log.d(APP_LOG, "response" + "\n" + responseLog + "\n" + bodyString)
            val jsonParts = bodyString.split("=")
            if (jsonParts.size >= 2) return response
                    .newBuilder()
                    .body(ResponseBody.create(response.body().contentType(),
                            "{\"${jsonParts[0]}\":\"${jsonParts[1].replace("|ID|1|:", "")}\"}"))
                    .build()
        }
        return response
                .newBuilder()
                .body(ResponseBody.create(response.body().contentType(), bodyString))
                .build()
    }
    companion object {
        fun bodyToString(request: Request): String {
            try {
                val copy = request.newBuilder().build()
                val buffer = Buffer()
                copy.body().writeTo(buffer)
                return buffer.readUtf8()
            } catch (e: IOException) {
                return "did not work"
            }
        }
    }
}