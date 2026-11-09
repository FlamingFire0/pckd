package me.flamingo.pckd.frontend.components.tree

import androidx.compose.runtime.Composable

interface TreeElement {
    fun name(): String
    
    @Composable
    fun content(depth: Int = 0) {}
}