package com.alterok.dataresult.error

import com.alterok.dataresult.DataResult

sealed class FileResultError(val message: String) : DataResult.IError {
    data object FileNotFound : FileResultError("File not found")
    data object ReadFailed : FileResultError("File read failed")
    data object WriteFailed : FileResultError("File write failed")
    data class Custom(val customMessage: String) : FileResultError(customMessage)

    companion object {
        fun fromMessage(message: String): FileResultError {
            return when (message) {
                "File not found" -> FileNotFound
                "File read failed" -> ReadFailed
                "File write failed" -> WriteFailed
                else -> Custom(message)
            }
        }
    }
}