package me.flamingo.pckd.frontend.utilities

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object DefaultScope : CoroutineScope {
    private val job = SupervisorJob()

    override val coroutineContext =
        job + Dispatchers.Main.immediate

    fun cancel() {
        job.cancel()
    }
}