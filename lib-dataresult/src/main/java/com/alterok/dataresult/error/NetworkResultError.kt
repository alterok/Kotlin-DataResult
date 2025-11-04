package com.alterok.dataresult.error

import com.alterok.dataresult.Constants
import com.alterok.dataresult.DataResult


sealed class NetworkResultError(
    val errorCode: Int,
    val message: String
) : DataResult.Error {

    override fun getErrorMessage(): String = "[$message, ErrorCode=$errorCode]"

    data object BadRequest : NetworkResultError(Constants.CODE_BAD_REQUEST, Constants.ERROR_MSG_BAD_REQUEST)

    data object Unauthorized : NetworkResultError(Constants.CODE_UNAUTHORIZED, Constants.ERROR_MSG_UNAUTHORIZED)

    data object Forbidden : NetworkResultError(Constants.CODE_FORBIDDEN, Constants.ERROR_MSG_FORBIDDEN)

    data object NotFound : NetworkResultError(Constants.CODE_NOT_FOUND, Constants.ERROR_MSG_NOT_FOUND)

    data object NoContent : NetworkResultError(Constants.CODE_NO_CONTENT, Constants.ERROR_MSG_NO_CONTENT)

    data object RequestTimeout : NetworkResultError(Constants.CODE_REQUEST_TIMEOUT, Constants.ERROR_MSG_REQUEST_TIMEOUT)

    data object TooManyRequests : NetworkResultError(Constants.CODE_TOO_MANY_REQUESTS, Constants.ERROR_MSG_TOO_MANY_REQUESTS)

    data object InternalServerError : NetworkResultError(Constants.CODE_INTERNAL_SERVER_ERROR, Constants.ERROR_MSG_INTERNAL_SERVER_ERROR)

    data object UnsupportedMediaType : NetworkResultError(Constants.CODE_UNSUPPORTED_MEDIA_TYPE, Constants.ERROR_MSG_UNSUPPORTED_MEDIA_TYPE)

    data object ServiceUnavailable : NetworkResultError(Constants.CODE_SERVICE_UNAVAILABLE, Constants.ERROR_MSG_SERVICE_UNAVAILABLE)

    class CustomError(
        errorCode: Int,
        msg: String
    ) : NetworkResultError(errorCode, msg) {
        override fun getErrorMessage(): String = "[$message, ErrorCode=$errorCode]"
    }

    companion object {
        private val codeMap = mapOf(
            Constants.CODE_BAD_REQUEST to BadRequest,
            Constants.CODE_UNAUTHORIZED to Unauthorized,
            Constants.CODE_FORBIDDEN to Forbidden,
            Constants.CODE_NOT_FOUND to NotFound,
            Constants.CODE_NO_CONTENT to NoContent,
            Constants.CODE_REQUEST_TIMEOUT to RequestTimeout,
            Constants.CODE_TOO_MANY_REQUESTS to TooManyRequests,
            Constants.CODE_UNSUPPORTED_MEDIA_TYPE to UnsupportedMediaType,
            Constants.CODE_INTERNAL_SERVER_ERROR to InternalServerError,
            Constants.CODE_SERVICE_UNAVAILABLE to ServiceUnavailable
        )

        fun fromCode(code: Int, msg: String? = null): NetworkResultError =
            codeMap[code] ?: CustomError(code, msg ?: "Unknown Error ($code)")
    }
}

