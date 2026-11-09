package me.flamingo.pckd.frontend.screens.editor.groupeditor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import me.flamingo.pckd.backend.key.Key
import me.flamingo.pckd.frontend.components.Panes
import me.flamingo.pckd.frontend.components.PanesState
import me.flamingo.pckd.frontend.components.tree.TreeGroup
import me.flamingo.pckd.frontend.components.tree.toTree
import me.flamingo.pckd.frontend.screens.Screen
import me.flamingo.pckd.frontend.screens.editor.Editor
import me.flamingo.pckd.frontend.theming.Mocha
import kotlin.io.path.name

class GroupEditor(val editor: Editor) : Screen(editor) {
    
    companion object {
        private val paneState = PanesState()
    }
    
    val removed = mutableStateListOf<Key>()
    
    @Composable
    fun TopBar() = Row {
        IconButton(onClick = ::previous) {
            Icon(imageVector = Icons.Rounded.KeyboardDoubleArrowLeft, contentDescription = "Back")
        }
    }
    
    @Composable
    fun DisabledPane() {
        TreeGroup(
            editor.rootDirectory.path.name, 
            removed.toTree {
                editor.keys += it
                removed -= it
            },
            false
        ).content()
    }
    
    @Composable
    fun EnabledPane() {
        TreeGroup(
            editor.rootDirectory.path.name, 
            editor.keys.toTree {
                editor.keys -= it
                removed += it
            }, 
            false
        ).content()
    }
    
    @Composable
    override fun Content() = Surface(color = Mocha.crust) {
        Column(Modifier.fillMaxSize()) {
            TopBar()

            Panes(
                state = paneState,
                left  = ::DisabledPane,
                right = ::EnabledPane,
            )
        }
    }
}