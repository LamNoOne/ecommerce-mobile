package com.selegend.ecommercemobile.core

import com.selegend.ecommercemobile.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

/**
 * Interceptor class for handling annotations.
 * This class intercepts the HTTP requests and modifies them based on the annotations present.
 */
class AnnotationInterceptor : Interceptor {

    /**
     * Intercepts the HTTP request and modifies it based on the annotations present.
     * @param chain The chain of interceptors.
     * @return The modified HTTP response.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val invocation =
            chain.request().tag(Invocation::class.java) ?: return chain.proceed(chain.request())
        containedOnInvocation(invocation).forEach { annotation ->
            if (request.header(Constants.X_API_VERSION) == null) {
                request = request.newBuilder().addHeader(
                    Constants.X_API_VERSION,
                    Constants.API_VERSION.toString()
                ).build()
            }
        }
        return chain.proceed(request)
    }

    /**
     * Retrieves the annotations present on the method being invoked.
     * @param invocation The method invocation.
     * @return The set of annotations present on the method.
     */
    private fun containedOnInvocation(invocation: Invocation): Set<Annotation> {
        return invocation.method().annotations.toSet()
    }
}