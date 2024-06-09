package com.alterok.dataresult.error

inline fun <reified T : Throwable> T.toExceptionResultError() = ExceptionResultError(this)