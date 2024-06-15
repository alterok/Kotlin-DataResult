package com.alterok.dataresult.error

import com.alterok.dataresult.DataResult

sealed class NetworkResultError(val errorCode: Int, val message: String) : DataResult.IError {
    data object BadRequest : NetworkResultError(400, "Bad Request") {
        override fun getErrorMessage(): String = "[$message, ErrorCode=$errorCode]"
    }

    data object Unauthorized : NetworkResultError(401, "Unauthorized") {
        override fun getErrorMessage(): String = "[$message, ErrorCode=$errorCode]"
    }

    data object Forbidden : NetworkResultError(403, "Forbidden") {
        override fun getErrorMessage(): String = "[$message, ErrorCode=$errorCode]"
    }

    data object NotFound : NetworkResultError(404, "Not Found") {
        override fun getErrorMessage(): String = "[$message, Error= $errorCode]"
    }

    data object NoContent : NetworkResultError(204, "No Content") {
        override fun getErrorMessage(): String = "[$message, Error= $errorCode]"
    }

    data object RequestTimeout : NetworkResultError(408, "Request Timeout") {
        override fun getErrorMessage(): String = "[$message, ErrorCode=$errorCode]"
    }

    data object TooManyRequests : NetworkResultError(429, "Too Many Requests") {
        override fun getErrorMessage(): String = "[$message, ErrorCode=$errorCode]"
    }

    data object InternalServerError : NetworkResultError(500, "Internal Server Error") {
        override fun getErrorMessage(): String = "[$message, ErrorCode=$errorCode]"
    }

    data object UnsupportedMediaType : NetworkResultError(415, "Unsupported Media Type") {
        override fun getErrorMessage(): String = "[$message, ErrorCode=$errorCode]"
    }

    data object ServiceUnavailable : NetworkResultError(503, "Service Unavailable") {
        override fun getErrorMessage(): String = "[$message, ErrorCode=$errorCode]"
    }

    data class CustomError(private val code: Int, private val msg: String) :
        NetworkResultError(code, msg) {
        override fun getErrorMessage(): String {
            return "[Network Error! ErrorCode=$code]"
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
                else -> CustomError(code, "CustomError($code)")
            }
        }
    }
}