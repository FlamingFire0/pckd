package me.flamingo.pckd.frontend.common.tree

import me.flamingo.pckd.backend.key.Key

private class TreeNode(
    val name: String
) {
    val children = linkedMapOf<String, TreeNode>()
    var key: Key? = null
}

fun Iterable<Key>.toTree(
    onSelect: (Key) -> Unit = {}
): List<TreeElement> {
    val roots = linkedMapOf<String, TreeNode>()

    for (key in this) {
        // Split namespace:path and the remaining directory segments.
        val segments = key.toString()
            .split(':', '/')
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

    fun build(node: TreeNode): TreeElement {
        val children = node.children.values.map(::build)

        return if (children.isEmpty()) {
            val key = requireNotNull(node.key)

            TreeEntry(
                name = node.name,
                onClick = { onSelect(key) }
            )
        } else {
            TreeGroup(
                name = node.name,
                children = children
            )
        }
    }

    return roots.values.map(::build)
}