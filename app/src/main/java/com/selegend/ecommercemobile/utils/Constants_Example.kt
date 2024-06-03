package com.selegend.ecommercemobile.utils

import com.google.android.gms.wallet.WalletConstants

object ConstantsExample {
    const val BASE_URL = "API_URL_HERE"
    const val API_VERSION = 2
    const val X_API_VERSION = "x-api-version"
    const val TOKEN_UID = 1
    const val GOOGLE_CLIENT_ID = "GOOGLE_CLIENT_ID_HERE"

    /**
     * Google Payment Configuration
     */
    /**
     * Changing this to ENVIRONMENT_PRODUCTION will make the API return chargeable card information.
     * Please refer to the documentation to read about the required steps needed to enable
     * ENVIRONMENT_PRODUCTION.
     *
     * @value #PAYMENTS_ENVIRONMENT
     */
    const val PAYMENTS_ENVIRONMENT = WalletConstants.ENVIRONMENT_TEST

    /**
     * The allowed networks to be requested from the API. If the user has cards from networks not
     * specified here in their account, these will not be offered for them to choose in the popup.
     *
     * @value #SUPPORTED_NETWORKS
     */
    val SUPPORTED_NETWORKS = listOf(
        "AMEX",
        "DISCOVER",
        "JCB",
        "MASTERCARD",
        "VISA"
    )

    /**
     * The Google Pay API may return cards on file on Google.com (PAN_ONLY) and/or a device token on
     * an Android device authenticated with a 3-D Secure cryptogram (CRYPTOGRAM_3DS).
     *
     * @value #SUPPORTED_METHODS
     */
    val SUPPORTED_METHODS = listOf(
        "PAN_ONLY",
        "CRYPTOGRAM_3DS"
    )

    /**
     * Required by the API, but not visible to the user.
     *
     * @value #COUNTRY_CODE Your local country
     */
    const val COUNTRY_CODE = "US"

    /**
     * Required by the API, but not visible to the user.
     *
     * @value #CURRENCY_CODE Your local currency
     */
    const val CURRENCY_CODE = "USD"

    /**
     * Supported countries for shipping (use ISO 3166-1 alpha-2 country codes). Relevant only when
     * requesting a shipping address.
     *
     * @value #SHIPPING_SUPPORTED_COUNTRIES
     */
    val SHIPPING_SUPPORTED_COUNTRIES = listOf("US", "GB", "VN")

    /**
     * The name of your payment processor/gateway. Please refer to their documentation for more
     * information.
     *
     * @value #PAYMENT_GATEWAY_TOKENIZATION_NAME
     */
    private const val PAYMENT_GATEWAY_TOKENIZATION_NAME = "braintree - REPLACE_ME"
    private const val GATEWAY_MERCHANT_ID = "MERCHANT_ID_HERE"

    /**
     * Custom parameters required by the processor/gateway.
     * In many cases, your processor / gateway will only require a gatewayMerchantId.
     * Please refer to your processor's documentation for more information. The number of parameters
     * required and their names vary depending on the processor.
     *
     * @value #PAYMENT_GATEWAY_TOKENIZATION_PARAMETERS
     */
    val PAYMENT_GATEWAY_TOKENIZATION_PARAMETERS = mapOf(
        "gateway" to PAYMENT_GATEWAY_TOKENIZATION_NAME,
        "braintree:apiVersion" to "v1",
        "braintree:sdkVersion" to "braintree.client.4.45.0",
        "braintree:merchantId" to GATEWAY_MERCHANT_ID,
        "braintree:clientKey" to "CLIENT_KEY_HERE"
    )

    /**
     * Only used for `DIRECT` tokenization. Can be removed when using `PAYMENT_GATEWAY`
     * tokenization.
     *
     * @value #DIRECT_TOKENIZATION_PUBLIC_KEY
     */
    const val DIRECT_TOKENIZATION_PUBLIC_KEY = "REPLACE_ME"

    /**
     * Parameters required for `DIRECT` tokenization.
     * Only used for `DIRECT` tokenization. Can be removed when using `PAYMENT_GATEWAY`
     * tokenization.
     *
     * @value #DIRECT_TOKENIZATION_PARAMETERS
     */
    val DIRECT_TOKENIZATION_PARAMETERS = mapOf(
        "protocolVersion" to "ECv1",
        "publicKey" to DIRECT_TOKENIZATION_PUBLIC_KEY
    )
}