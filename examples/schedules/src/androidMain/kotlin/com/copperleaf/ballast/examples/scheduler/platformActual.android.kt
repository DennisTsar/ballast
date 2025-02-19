package com.copperleaf.ballast.examples.scheduler

import com.copperleaf.ballast.BallastLogger
import com.copperleaf.ballast.core.AndroidLogger
import com.copperleaf.ballast.debugger.BallastDebuggerClientConnection
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

private val lazyConnection by lazy {
    BallastDebuggerClientConnection(
        engineFactory = CIO,
        applicationCoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
        host = "10.0.2.2",
    ) {
        // CIO Ktor client engine configuration
    }.also { it.connect() }
}

internal actual fun platformDebuggerConnection(): BallastDebuggerClientConnection<*> {
    return lazyConnection
}

internal actual fun platformLogger(loggerName: String): BallastLogger {
    return AndroidLogger(loggerName)
}
