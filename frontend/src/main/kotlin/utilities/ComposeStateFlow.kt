package me.flamingo.pckd.frontend.utilities

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
class ComposeStateFlow<T>(
    initialValue: T,
    private val scope: CoroutineScope = DefaultScope
) : MutableState<T>, StateFlow<T> {

    private val composeState = mutableStateOf(initialValue)
    private val flowState = MutableStateFlow(initialValue)

    init {
        scope.launch {
            flowState.collect { newValue ->
                if (composeState.value != newValue) {
                    composeState.value = newValue
                }
            }
        }
    }

    override var value: T
        get() = composeState.value
        set(value) {
            composeState.value = value
            flowState.value = value
        }

    override val replayCache: List<T>
        get() = flowState.replayCache

    override suspend fun collect(
        collector: FlowCollector<T>
    ): Nothing = flowState.collect(collector)

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }

    override fun component1(): T = value

    override fun component2(): (T) -> Unit = { value = it }
}