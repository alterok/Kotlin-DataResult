package com.alterok.dataresult.error

import com.alterok.dataresult.Constants
import com.alterok.dataresult.DataResult

sealed class FileResultError(val message: String) : DataResult.Error {
    data object FileNotFound : FileResultError(Constants.ERROR_MSG_FILE_NOT_FOUND) {
        override fun getErrorMessage(): String = message
    }

    data object ReadFailed : FileResultError(Constants.ERROR_MSG_FILE_READ_FAILED){
        override fun getErrorMessage(): String = message
    }
    data object WriteFailed : FileResultError(Constants.ERROR_MSG_FILE_WRITE_FAILED){
        override fun getErrorMessage(): String = message
    }
    data class Custom(val customMessage: String) : FileResultError(customMessage){
        override fun getErrorMessage(): String = customMessage
    }

    companion object {
        fun fromMessage(message: String): FileResultError {
            return when (message) {
                Constants.ERROR_MSG_FILE_NOT_FOUND -> FileNotFound
                Constants.ERROR_MSG_FILE_READ_FAILED -> ReadFailed
                Constants.ERROR_MSG_FILE_WRITE_FAILED -> WriteFailed
                else -> Custom(message)
            }
        }
    }
}