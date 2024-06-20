package com.alterok.dataresult

import com.alterok.dataresult.error.ExceptionResultError

/**
 * Represents a result of an operation that can be in one of three states: Loading, Success or Failure.
 * - [Loading] indicates ongoing operation with optional data of type [D].
 * - [Success] encapsulates successful operation with associated data of type [D].
 * - [Failure] signifies a failed operation with associated error of type [IError] and optional data of type [D].
 *
 * @param D The type of data associated with Success, Loading, and Failure states.
 */
sealed class DataResult<D> {
    interface IError {
        fun getErrorMessage(): String
    }

    data class Loading<D>(val data: D? = null) : DataResult<D>()
    data class Success<D>(val data: D) : DataResult<D>()
    data class Failure<D, out E : IError>(val error: E, val data: D? = null) : DataResult<D>()

    val isSuccess: Boolean get() = this is Success
    val isLoading: Boolean get() = this is Loading
    val isFailure: Boolean get() = this is Failure<D, IError>

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

    fun getOrNull(): D? {
        return when (this) {
            is Success -> data
            is Loading -> data
            is Failure<D, IError> -> data
        }
    }

    /**
     * Transforms the data contained within this `DataResult`.
     *
     * If the transformation returns `null` for [DataResult.Success], the result will become a `Failure` of [ExceptionResultError].
     *
     *
     * @param transform A function that takes the current data of type `D` and transforms it into a new value of type `R`.
     * @return A `DataResult<R>` containing the transformed data if successful, or the appropriate `Loading` or `Failure` state.
     *
     *
     * Example usage:
     * ```
     * val result: DataResult<OuterData> = DataResult.Success(OuterData(InnerData("Hello")))
     * val mappedResult: DataResult<String> = result.map { it.innerData?.value }
     * // mappedResult will be DataResult.Success("Hello") if innerData is non-null, otherwise DataResult.Failure
     * ```
     */
    inline fun <R> map(transform: (D) -> R?): DataResult<R> {
        return when (this) {
            is Success -> {
                val data: R? = transform(data)
                if (data != null)
                    Success(data)
                else
                    Failure(ExceptionResultError.NullTransformation)
            }

            is Loading -> Loading(data?.let(transform))
            is Failure<D, IError> -> Failure(
                error = error,
                data = data?.let(transform)
            )
        }
    }

    /**
     * Transforms this `DataResult`. The transformation function returns another `DataResult`.
     *
     * @param transform A function that takes the current data:D? and returns a `DataResult<R>`.
     * @return A new `DataResult<R>` from the `transform` function.
     *
     * Example usage:
     * ```
     * val result: DataResult<Int> = DataResult.Success(10)
     * val mappedResult: DataResult<String> = result.flatMap { number ->
     *     if (number > 0) {
     *         DataResult.Success("Positive")
     *     } else {
     *         DataResult.Failure(ExceptionResultError(Exception("Number is not positive")))
     *     }
     * }
     * // mappedResult will be DataResult.Success("Positive") if the original result was a success and the number was positive.
     * ```
     */
    inline fun <R> flatMap(transform: (D?) -> DataResult<R>): DataResult<R> {
        return transform(getOrNull())
    }

    inline fun <R> flatMap2(transform: DataResult<D>.(D?) -> DataResult<R>): DataResult<R> {
        return transform(getOrNull())
    }

    inline fun <R> transform(transform: DataResult<D>.() -> DataResult<R>): DataResult<R> {
        return transform()
    }

    /**
     * Applies a recovery function `transform` to convert a `Failure` into a `Success` state.
     * If the `DataResult` is already in a `Success` or 'Loading' state, it remains unchanged.
     *
     * @param transform A function that takes the error of type `IError` and returns a new value of type `D`.
     * @return A `DataResult<D>` either unchanged or transformed from a `Failure` to a `Success` state.
     *
     * Example usage:
     * ```
     * val result: DataResult<String> = DataResult.Failure(ExceptionResultError(Exception("Error message")))
     * val recoveredResult: DataResult<String> = result.recover { error ->
     *     "Recovered value from error: ${error.getErrorMessage()}"
     * }
     * // recoveredResult will be DataResult.Success("Recovered value from error: Error message")
     * ```
     */
    inline fun recover(transform: (error: IError) -> D): DataResult<D> {
        return when (this) {
            is Failure<D, IError> -> Success(transform(error))
            else -> this
        }
    }

    /**
     * Applies a recovery function `transform` to convert a `Failure` into another `DataResult`.
     * If the `DataResult` is already in a `Success` or 'Loading' state, it remains unchanged.
     *
     * @param transform A function that takes the error of type `IError` and returns another `DataResult`.
     * @return A `DataResult<D>` either unchanged or transformed from a `Failure` to another `DataResult`.
     *
     * Example usage:
     * ```
     * val result: DataResult<String> = DataResult.Failure(ExceptionResultError(Exception("Error message")))
     * val recoveredResult: DataResult<String> = result.recoverWith { error ->
     *     DataResult.Success("Recovered value from error: ${error.getErrorMessage()}")
     * }
     * // recoveredResult will be DataResult.Success("Recovered value from error: Error message")
     * ```
     */
    inline fun recoverWith(transform: (error: IError) -> DataResult<D>): DataResult<D> {
        return if (isFailure) {
            transform((this as Failure<D, IError>).error)
        } else {
            this
        }
    }

    override fun toString(): String {
        return "DataResult.${
            when (this) {
                is Loading -> "Loading($data)"
                is Success -> "Success($data)"
                is Failure<D, *> -> "Failure(${error.getErrorMessage()} , $data)"
            }
        }"
    }
}

fun <T> T?.wrapInLoadingDataResult() = DataResult.Loading(this)
inline fun <reified T> T.wrapInSuccessDataResult() = DataResult.Success(this)
fun <T> T?.wrapInFailureDataResult(error: DataResult.IError) = DataResult.Failure(error, this)
fun <D> DataResult.IError.wrapErrorInFailureDataResult(data: D? = null) =
    DataResult.Failure(this, data)

/**
 * Runs a block of code that may throw an exception and wraps the result in a `DataResult`.
 * If the block executes successfully, returns a `DataResult.Success` with the result of the block.
 * If an exception is thrown during the execution of the block, returns a `DataResult.Failure`
 * wrapping an `ExceptionResultError` containing the thrown exception.
 *
 * @param block A function that produces a result of type `D` and may throw an `Exception`.
 * @return A `DataResult<D>` representing the success or failure state of the block execution.
 *
 * Example usage:
 * ```
 * val result: DataResult<Int> = runCatchingForDataResult {
 *     val randomNumber = Random.nextInt(1, 10)
 *     if (randomNumber > 5) {
 *         randomNumber
 *     } else {
 *         throw Exception("Random number is not greater than 5")
 *     }
 * }
 * // result will be DataResult.Success(randomNumber) if randomNumber > 5, otherwise DataResult.Failure wrapping the caught exception.
 * ```
 */
inline fun <D> runCatchingForDataResult(block: () -> D): DataResult<D> {
    return try {
        DataResult.Success(block())
    } catch (e: Exception) {
        DataResult.Failure(ExceptionResultError.Custom(e))
    }
}