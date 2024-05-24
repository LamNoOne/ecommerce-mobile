package com.example.ecommercemobile.core

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.InetAddress

class ForceCacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        if (!isInternetAvailable()) {
            builder.cacheControl(CacheControl.FORCE_CACHE);
        }
        return chain.proceed(builder.build());
    }

    private fun isInternetAvailable(): Boolean {
        return try {
            val address : InetAddress = InetAddress.getByName("google.com")
            //You can replace it with your name
            !address.equals("")
        } catch (e: Exception) {
            false
        }
    }
}
