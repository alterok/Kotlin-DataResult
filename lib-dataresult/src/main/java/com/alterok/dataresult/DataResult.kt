package com.alterok.dataresult

sealed class DataResult<D> {
    data class Loading<D>(val data: D? = null) : DataResult<D>()
    data class Success<D>(val data: D) : DataResult<D>()
    data class Failure<D, out E : IError>(val error: E, val data: D? = null) : DataResult<D>()
    interface IError{
        override fun toString(): String
    }

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

    override fun toString(): String {
        return "DataResult.${
            when (this) {
                is Failure<*, *> -> "Failure(${error} , $data)"
                is Loading -> "Loading($data)"
                is Success -> "Success($data)"
            }
        }"
    }
}

fun <T> T?.wrapInLoadingDataResult() = DataResult.Loading(this)
inline fun <reified T> T.wrapInSuccessDataResult() = DataResult.Success(this)
fun <T> T?.wrapInFailureDataResult(error: DataResult.IError) = DataResult.Failure(error, this)

