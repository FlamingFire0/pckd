package me.flamingo.pckd.frontend.common.tree

import androidx.compose.runtime.Composable
import me.flamingo.pckd.frontend.common.ComposeElement

interface TreeElement : ComposeElement {
    
    fun name(): String
    
    @Composable
    override fun content() = content(0)

    @Composable
    fun content(depth: Int) {}
}