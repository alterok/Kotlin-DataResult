package com.alterok.dataresult

sealed class DataResult<D> {
    data class Loading<D>(val data: D? = null) : DataResult<D>()
    data class Success<D>(val data: D) : DataResult<D>()
    data class Failure<out E : Error, D>(val error: E, val data: D? = null) : DataResult<D>()
    interface Error
}