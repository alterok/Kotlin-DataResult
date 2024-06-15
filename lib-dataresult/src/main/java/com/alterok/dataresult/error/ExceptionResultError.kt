package com.alterok.dataresult.error

import com.alterok.dataresult.DataResult

sealed class ExceptionResultError private constructor(val exception: Throwable) : DataResult.IError {
    data object NullTransformation : ExceptionResultError(Exceptions.NullTransformationException())
    data class Custom(private val e: Throwable): ExceptionResultError(e)

    override fun getErrorMessage(): String {
        return exception.message ?: exception.toString()
    }

    class Exceptions {
        class NullTransformationException : Throwable("Failure! Transformation results in null value.")
    }
}

inline fun <reified T : Throwable> T.toExceptionResultError() = ExceptionResultError.Custom(this)
