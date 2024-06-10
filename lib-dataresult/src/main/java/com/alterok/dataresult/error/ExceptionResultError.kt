package com.alterok.dataresult.error

import com.alterok.dataresult.DataResult

class ExceptionResultError(val exception: Throwable) : DataResult.IError {
    override fun toString(): String {
        return "ExceptionResultError($exception)"
    }
}

inline fun <reified T : Throwable> T.toExceptionResultError() = ExceptionResultError(this)