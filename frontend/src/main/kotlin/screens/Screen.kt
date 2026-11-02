package me.flamingo.pckd.frontend.screens

import me.flamingo.pckd.frontend.App
import me.flamingo.pckd.frontend.common.ComposeElement

abstract class Screen(
    val previous: Screen? = null
) : ComposeElement {
    fun previous() = previous?.open()
    
    fun open(): Screen {
        App.screen = this
        return this
    }
    
    fun next(block: (Screen) -> Screen) = block(this).open()
}