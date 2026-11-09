package me.flamingo.pckd.frontend.screens

import androidx.compose.runtime.Composable
import me.flamingo.pckd.frontend.App

abstract class Screen(
    val previous: Screen? = null
) {
    @Composable
    abstract fun Content()

    fun previous() = previous?.open()
    
    fun open(): Screen {
        App.screen = this
        return this
    }
    
    fun next(block: (Screen) -> Screen) = block(this).open()
}