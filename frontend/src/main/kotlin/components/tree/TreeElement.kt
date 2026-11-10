package me.flamingo.pckd.frontend.components.tree

import androidx.compose.runtime.Composable

interface TreeElement {
    fun name(): String
    
    var parent: TreeGroup?
    
    fun addParents(parents: List<TreeGroup>) {
        parents.fold(this) { acc, group -> acc.parent = group; group }
    }
    
    @Composable
    fun content(depth: Int = 0) {}
}