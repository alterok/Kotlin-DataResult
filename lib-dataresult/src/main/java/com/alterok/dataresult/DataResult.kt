package com.alterok.dataresult

sealed class DataResult<D> {
    data class Loading<D>(val data: D? = null)
    data class Success<D>(val data: D)
    data class Failure<out E : Error, D>(val error: E, val data: D? = null)
    interface Error
}