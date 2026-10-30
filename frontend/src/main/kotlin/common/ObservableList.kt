package me.flamingo.pckd.frontend.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

class ObservableList<T>(
    private val backing: MutableCollection<T>,
    private val order: Comparator<in T>? = null   // optional sorted view
) : AbstractMutableList<T>() {

    private val backingList = backing as? MutableList<T>   // fast path when it's already indexed

    private var version by mutableIntStateOf(0)
    private var cache: List<T> = emptyList()
    private var cachedVersion = -1

    private fun changed() { version++ }

    /** Reading `version` registers the Compose dependency. */
    private fun snapshot(): List<T> {
        val v = version
        if (cachedVersion != v) {
            cache = if (order != null) backing.sortedWith(order) else backing.toList()
            cachedVersion = v
        }
        return cache
    }

    override val size: Int
        get() { version; return backing.size }

    override fun get(index: Int): T =
        if (backingList != null && order == null) { version; backingList[index] }
        else snapshot()[index]

    override fun contains(element: T): Boolean { version; return backing.contains(element) }

    // --- mutations: always go through the backing collection, then invalidate ---

    override fun add(element: T): Boolean =
        backing.add(element).also { if (it) changed() }

    /** Position is meaningless for unordered collections, so for non-lists the index is ignored. */
    override fun add(index: Int, element: T) {
        if (backingList != null && order == null) backingList.add(index, element)
        else backing.add(element)
        changed()
    }

    override fun remove(element: T): Boolean =
        backing.remove(element).also { if (it) changed() }

    override fun removeAt(index: Int): T {
        val removed = if (backingList != null && order == null) backingList.removeAt(index)
        else snapshot()[index].also { backing.remove(it) }
        changed()
        return removed
    }

    override fun set(index: Int, element: T): T {
        val old: T
        if (backingList != null && order == null) {
            old = backingList.set(index, element)
        } else {
            old = snapshot()[index]
            backing.remove(old)
            backing.add(element)     // for sets: no-op if already present, so size can shrink
        }
        changed()
        return old
    }

    override fun clear() {
        if (backing.isNotEmpty()) { backing.clear(); changed() }
    }

    /** Call when the backing collection was mutated by someone else. */
    fun invalidate() = changed()
}

fun <T> MutableCollection<T>.asObservableList(order: Comparator<in T>? = null) =
    ObservableList(this, order)