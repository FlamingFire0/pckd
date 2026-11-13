package me.flamingo.pckd.frontend.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveableStateHolder

interface Screen {
    
    @Composable
    fun Content()

    companion object {
        val defaultScreen = Home
        
        private class ScreenEntry(val screen: Screen)
        
        private val screenStack = mutableStateListOf(ScreenEntry(defaultScreen))
        private val currentEntry: ScreenEntry get() = screenStack.last()

        private val popped = mutableListOf<ScreenEntry>()

        val current: Screen get() = currentEntry.screen
        
        fun open(screen: Screen) {
            screenStack.add(ScreenEntry(screen))
        }
        
        fun back(): Boolean {
            if (screenStack.size <= 1) return false
            popped += screenStack.removeLast()
            return true
        }
        
        fun replace(screen: Screen) {
            back()
            open(screen)
        }
        
        @Composable
        fun Render() {
            val holder = rememberSaveableStateHolder()
            val screenEntry = currentEntry
            
            holder.SaveableStateProvider(screenEntry) {
                screenEntry.screen.Content()
            }

            SideEffect {
                popped.forEach { holder.removeState(it) }
                popped.clear()
            }
        }
    }
}