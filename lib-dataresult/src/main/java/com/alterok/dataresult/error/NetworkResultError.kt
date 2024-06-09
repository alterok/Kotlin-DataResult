package com.alterok.dataresult.error

import com.alterok.dataresult.DataResult

sealed class NetworkResultError(val errorCode: Int) : DataResult.Error {
    data object BadRequest : NetworkResultError(400)
    data object Unauthorized : NetworkResultError(401)
    data object Forbidden : NetworkResultError(403)
    data object NotFound : NetworkResultError(404)
    data object RequestTimeout : NetworkResultError(408)
    data object TooManyRequests : NetworkResultError(429)
    data object InternalServerError : NetworkResultError(500)
    data object ServiceUnavailable : NetworkResultError(503)

    data class CustomError(private val code: Int) : NetworkResultError(code)

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