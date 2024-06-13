package com.alterok.dataresult.error

import com.alterok.dataresult.DataResult

class ExceptionResultError(val exception: Throwable) : DataResult.IError {
    override fun getErrorMessage(): String {
        return exception.message ?: exception.toString()
    }

    override fun toString(): String {
        return "ExceptionResultError($exception)"
    }
}

inline fun <reified T : Throwable> T.toExceptionResultError() = ExceptionResultError(this)