package com.eatigo.common.coroutines

import android.os.Handler
import android.os.Looper
import kotlin.coroutines.experimental.AbstractCoroutineContextElement
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.ContinuationInterceptor

object Android : AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor {
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> = AndroidContinuation(continuation)
}

private class AndroidContinuation<T>(val cont: Continuation<T>) : Continuation<T> by cont {
    override fun resume(value: T) {
        when (Looper.myLooper() == Looper.getMainLooper()) {
            true -> cont.resume(value)
            false -> Handler(Looper.getMainLooper()).post { cont.resume(value) }
        }
    }

    override fun resumeWithException(exception: Throwable) {
        when (Looper.myLooper() == Looper.getMainLooper()) {
            true -> cont.resumeWithException(exception)
            false -> Handler(Looper.getMainLooper()).post { cont.resumeWithException(exception) }
        }
    }
}


