package me.flamingo.pckd.frontend.components.tree

import androidx.compose.runtime.MutableState
import me.flamingo.pckd.backend.key.Key
import me.flamingo.pckd.frontend.components.contextmenu.ContextAction

private class TreeNode(val name: String) {
    val children = linkedMapOf<String, TreeNode>()
    var key: Key? = null
}

fun Iterable<Key>.toTree(
    autoSelect: ((Key) -> Boolean) = { false },
    selectedVariable: MutableState<TreeEntry?>? = null,
    contextActions: MutableList<ContextAction>.(TreeEntry) -> Unit = {},
    onSelect: TreeEntry.(Key) -> Unit = {},
): List<TreeElement> {
    val roots = linkedMapOf<String, TreeNode>()

    for (key in this) {
        // Split namespace:path and the remaining directory segments.
        val segments = key.toString()
            .split(Key.NAMESPACE_DELIMITER, Key.PATH_DELIMITER)
            .filter { it.isNotBlank() }

        if (segments.isEmpty()) continue

        var siblings = roots
        var node: TreeNode? = null

        for (segment in segments) {
            val child = siblings.getOrPut(segment) {
                TreeNode(segment)
            }

            node = child
            siblings = child.children
        }

        node?.key = key
    }

    fun build(node: TreeNode, parent: TreeGroup?): TreeElement {
        if (node.children.isEmpty()) {
            val key = requireNotNull(node.key) { "Leaf node '${node.name}' has no key" }
            val selected = autoSelect(key)



            val entry = TreeEntry(
                name = node.name,
                key = key,
                parent = parent,
                selected = selected,
                contextActions = contextActions,
                onClick = { it.onSelect(key) },
            )

            if (selected) {
                selectedVariable?.value?.isSelected = false
                selectedVariable?.value = entry
            }

            return entry
        }

        val group = TreeGroup(
            name = node.name,
            parent = parent,
        )

        node.children.values.mapTo(group.children) { build(it, group) }

        return group
    }

    return roots.values.map { build(it, null) }
}