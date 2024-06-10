package com.alterok.dataresult.error

import com.alterok.dataresult.DataResult

sealed class PermissionResultError(val message: String) : DataResult.IError {
    data object PermissionDenied : PermissionResultError("Permission denied")
    data object PermissionRevoked : PermissionResultError("Permission revoked")

    data class Custom(val customMessage: String) : PermissionResultError(customMessage)

    companion object {
        fun fromMessage(message: String): PermissionResultError {
            return when (message) {
                "Permission denied" -> PermissionDenied
                "Permission revoked" -> PermissionRevoked
                else -> Custom(message)
            }
        }
    }
}