package com.alterok.dataresult.error

import com.alterok.dataresult.Constants
import com.alterok.dataresult.DataResult

sealed class PermissionResultError(val message: String) : DataResult.Error {
    data object PermissionDenied : PermissionResultError(Constants.ERROR_MSG_PERMISSION_DENIED) {
        override fun getErrorMessage() = message
    }

    data object PermissionRevoked : PermissionResultError(Constants.ERROR_MSG_PERMISSION_REVOKED) {
        override fun getErrorMessage() = message
    }

    data class Custom(val customMessage: String) : PermissionResultError(customMessage) {
        override fun getErrorMessage() = message
    }

    companion object {
        fun fromMessage(message: String): PermissionResultError {
            return when (message) {
                Constants.ERROR_MSG_PERMISSION_DENIED -> PermissionDenied
                Constants.ERROR_MSG_PERMISSION_REVOKED -> PermissionRevoked
                else -> Custom(message)
            }
        }
    }
}