package com.alterok.dataresult.error

import com.alterok.dataresult.DataResult

sealed class NetworkResultError(val errorCode: Int) : DataResult.IError {
    data object BadRequest : NetworkResultError(400) {
        override fun getErrorMessage(): String = "Bad Request, Error=$errorCode"
    }

    data object Unauthorized : NetworkResultError(401) {
        override fun getErrorMessage(): String = "Unauthorized, Error=$errorCode"
    }
    data object Forbidden : NetworkResultError(403) {
        override fun getErrorMessage(): String = "Forbidden, Error=$errorCode"
    }
    data object NotFound : NetworkResultError(404) {
        override fun getErrorMessage(): String = "Not Found, Error= $errorCode"
    }
    data object RequestTimeout : NetworkResultError(408) {
        override fun getErrorMessage(): String = "Request Timeout, Error=$errorCode"
    }
    data object TooManyRequests : NetworkResultError(429) {
        override fun getErrorMessage(): String = "Too Many Requests, Error=$errorCode"
    }
    data object InternalServerError : NetworkResultError(500) {
        override fun getErrorMessage(): String = "Internal Server Error, Error=$errorCode"
    }
    data object ServiceUnavailable : NetworkResultError(503) {
        override fun getErrorMessage(): String = "Service Unavailable, Error=$errorCode"
    }

    data class CustomError(private val code: Int) : NetworkResultError(code){
        override fun getErrorMessage(): String {
            return "Network Error! Error=$code"
        }
    }

    companion object {
        fun fromCode(code: Int): NetworkResultError {
            return when (code) {
                400 -> BadRequest
                401 -> Unauthorized
                403 -> Forbidden
                404 -> NotFound
                408 -> RequestTimeout
                429 -> TooManyRequests
                500 -> InternalServerError
                503 -> ServiceUnavailable
                else -> CustomError(code)
            }
        }
    }
}