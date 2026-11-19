package me.flamingo.pckd.frontend.utilities

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.flamingo.pckd.backend.config.ConfigFile


fun <T : Any> ConfigFile<T>.asComposeState(
    scope: CoroutineScope = DefaultScope
): MutableState<T> {
    val config = this
    val backing = mutableStateOf(value)

    scope.launch {
        state.collect {
            backing.value = it
        }
    }

    return object : MutableState<T> {
        override var value: T
            get() = backing.value
            set(value) {
                config.save(value)
            }

        override fun component1(): T = value
        override fun component2(): (T) -> Unit = { value = it }
    }
}