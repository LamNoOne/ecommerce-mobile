package com.example.ecommercemobile.core

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.ecommercemobile.store.domain.model.core.Auth
import com.example.ecommercemobile.store.domain.repository.AuthRepository
import com.example.ecommercemobile.utils.Constants
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation
import javax.inject.Inject

class AnnotationInterceptor: Interceptor {
    @Inject
    lateinit var authRepository: AuthRepository

    private var authData by mutableStateOf<Auth?>(null)

    init {
        runBlocking {
            authRepository.getAuth(1)?.let {
                this@AnnotationInterceptor.authData = it
            }
        }
    }

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
            .addHeader("x-user-id", getUserIdFromDB().toString())
            .addHeader("Authorization", "Bearer $token")
            .build()
    }

    private fun getTokenFromDB(): String {
        return authData?.accessToken ?: ""
    }

    private fun getUserIdFromDB(): Int {
        return authData?.userId ?: 0
    }
}