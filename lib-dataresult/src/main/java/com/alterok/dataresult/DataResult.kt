package com.alterok.dataresult

import com.alterok.dataresult.Constants.CODE_SUCCESS_OK
import com.alterok.dataresult.error.ExceptionResultError

/**
 * Represents a result of an operation that can be in one of three states: Loading, Success or Failure.
 * - [Idle] indicates no ongoing operation with optional data of type [D].
 * - [Loading] indicates ongoing operation with optional data of type [D].
 * - [Success] encapsulates successful operation with associated data of type [D].
 * - [Failure] signifies a failed operation with associated error of type [Error] and optional data of type [D].
 *
 * @param D The type of data associated with Success, Loading, and Failure states.
 */
sealed class DataResult<D> {
    interface Error {
        fun getErrorMessage(): String
    }

    data class Idle<D>(val data: D? = null) : DataResult<D>()
    data class Loading<D>(val data: D? = null) : DataResult<D>()
    data class Success<D>(val data: D, val code: Int = CODE_SUCCESS_OK) : DataResult<D>()
    data class Failure<D, out E : Error>(val error: E, val data: D? = null) : DataResult<D>()

    val isIdle: Boolean get() = this is Idle
    val isLoading: Boolean get() = this is Loading
    val isSuccess: Boolean get() = this is Success
    val isFailure: Boolean get() = this is Failure<D, Error>

    inline fun onIdle(block: (D?) -> Unit): DataResult<D> {
        if (this is Loading)
            block(data)
        return this
    }

    inline fun onLoading(block: (D?) -> Unit): DataResult<D> {
        if (this is Loading)
            block(data)
        return this
    }

    inline fun onSuccess(block: (D, Int) -> Unit): DataResult<D> {
        if (this is Success)
            block(data, code)
        return this
    }

    inline fun onFailure(block: (Error, D?) -> Unit): DataResult<D> {
        if (this is Failure<D, Error>)
            block(error, data)
        return this
    }

    fun getOrNull(): D? {
        return when (this) {
            is Idle -> data
            is Success -> data
            is Loading -> data
            is Failure<D, Error> -> data
        }
    }

    fun getErrorOrNull(): Error? {
        return when (this) {
            is Idle -> null
            is Loading -> null
            is Success -> null
            is Failure<D, Error> -> error
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

            is Idle -> Idle(data?.let(transform))
            is Loading -> Loading(data?.let(transform))
            is Failure<D, Error> -> Failure(
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
    inline fun recover(transform: (error: Error) -> D): DataResult<D> {
        return when (this) {
            is Failure<D, Error> -> Success(transform(error))
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
    inline fun recoverWith(transform: (error: Error) -> DataResult<D>): DataResult<D> {
        return if (isFailure) {
            transform((this as Failure<D, Error>).error)
        } else {
            this
        }
    }

    fun Success<D>.withCode(code: Int) = Success(this.data, code)

    override fun toString(): String {
        return "DataResult.${
            when (this) {
                is Idle -> "Idle($data)"
                is Loading -> "Loading($data)"
                is Success -> "Success($code | $data)"
                is Failure<D, *> -> "Failure(${error.getErrorMessage()} , $data)"
            }
        }"
    }
}

fun <T> T?.wrapInIdleDataResult() = DataResult.Idle(this)
fun <T> T?.wrapInLoadingDataResult() = DataResult.Loading(this)
inline fun <reified T> T.wrapInSuccessDataResult(code: Int = CODE_SUCCESS_OK) = DataResult.Success(this, code)
fun <T> T?.wrapInFailureDataResult(error: DataResult.Error) = DataResult.Failure(error, this)
fun <D> DataResult.Error.wrapErrorInFailureDataResult(data: D? = null) =
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