package com.alterok.dataresult

fun <T> T?.wrapInLoadingDataResult() = DataResult.Loading(this)
inline fun <reified T : Any> T.wrapInSuccessDataResult() = DataResult.Success(this)
fun <T> T?.wrapInFailureDataResult(error: DataResult.Error) = DataResult.Failure(error, this)
