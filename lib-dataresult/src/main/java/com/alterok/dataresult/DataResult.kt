package com.alterok.dataresult

import com.alterok.dataresult.error.ExceptionResultError
import javax.xml.crypto.Data

sealed class DataResult<D> {
    interface IError {
        fun getErrorMessage(): String
        override fun toString(): String
    }

    data class Loading<D>(val data: D? = null) : DataResult<D>()
    data class Success<D>(val data: D) : DataResult<D>()
    data class Failure<D, out E : IError>(val error: E, val data: D? = null) : DataResult<D>()

    val isSuccess: Boolean get() = this is Success
    val isLoading: Boolean get() = this is Loading
    val isFailure: Boolean get() = this is IError

    fun getOrNull(): D? {
        return when (this) {
            is Success -> data
            is Loading -> data
            is Failure<D, IError> -> data
        }
    }

    inline fun <R> map(transform: (D) -> R): DataResult<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Loading -> Loading(data?.let(transform))
            is Failure<D, IError> -> Failure(
                error = error,
                data = data?.let(transform)
            )
        }
    }

    inline fun <R> flatMap(transform: (D?) -> DataResult<R>): DataResult<R> {
        return transform(getOrNull())
    }

    inline fun onLoading(block: (D?) -> Unit): DataResult<D> {
        if (this is Loading)
            block(data)
        return this
    }

    inline fun onSuccess(block: (D) -> Unit): DataResult<D> {
        if (this is Success)
            block(data)
        return this
    }

    inline fun onFailure(block: (IError, D?) -> Unit): DataResult<D> {
        if (this is Failure<D, IError>)
            block(error, data)
        return this
    }

    override fun toString(): String {
        return "DataResult.${
            when (this) {
                is Loading -> "Loading($data)"
                is Success -> "Success($data)"
                is Failure<D, *> -> "Failure(${error} , $data)"
            }
        }"
    }
}

fun <T> T?.wrapInLoadingDataResult() = DataResult.Loading(this)
inline fun <reified T> T.wrapInSuccessDataResult() = DataResult.Success(this)
fun <T> T?.wrapInFailureDataResult(error: DataResult.IError) = DataResult.Failure(error, this)

inline fun <D> runCatchingForDataResult(block: () -> D): DataResult<D> {
    return try {
        DataResult.Success(block())
    } catch (e: Exception) {
        DataResult.Failure(ExceptionResultError(e))
    }
}
