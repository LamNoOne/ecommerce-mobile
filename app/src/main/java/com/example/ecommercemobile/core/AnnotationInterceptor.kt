package com.example.ecommercemobile.core

import com.example.ecommercemobile.utils.Constants
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation

class AnnotationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val invocation =
            chain.request().tag(Invocation::class.java) ?: return chain.proceed(chain.request())
        containedOnInvocation(invocation).forEach { annotation ->
            request = request.newBuilder().addHeader(
                "x-api-version",
                Constants.API_VERSION.toString()
            ).build()
            request = handleAnnotation(annotation, request)
        }
        return chain.proceed(request)
    }

    private fun containedOnInvocation(invocation: Invocation): Set<Annotation> {
        return invocation.method().annotations.toSet()
    }

    private fun handleAnnotation(
        annotation: Annotation,
        request: Request,
    ): Request {
        return when (annotation) {
            is Authorized -> addAuthHeader(request)
            else -> request
        }
    }

    private fun addAuthHeader(request: Request): Request {
        val token = getTokenFromDB()
        return request.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
    }

    private fun getTokenFromDB(): String {
        // get Token from Room DB -> Handle later...
        return "token"
    }
}