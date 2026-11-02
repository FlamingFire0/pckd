package me.flamingo.pckd.frontend.screens.editor.group_editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import me.flamingo.pckd.backend.key.Key
import me.flamingo.pckd.frontend.Mocha
import me.flamingo.pckd.frontend.common.PanesState
import me.flamingo.pckd.frontend.common.panes
import me.flamingo.pckd.frontend.common.tree.TreeGroup
import me.flamingo.pckd.frontend.common.tree.toTree
import me.flamingo.pckd.frontend.screens.Screen
import me.flamingo.pckd.frontend.screens.editor.Editor
import kotlin.io.path.name

class GroupEditor(val editor: Editor) : Screen(editor) {
    
    companion object {
        private val paneState = PanesState()
    }
    
    val removed = mutableStateListOf<Key>()
    
    @Composable
    override fun content() = Column(Modifier.fillMaxSize()) {
        Surface(Modifier.fillMaxWidth(), color = Mocha.crust) {
            Row {
                IconButton(onClick = ::previous) {
                    Icon(imageVector = Icons.Rounded.KeyboardDoubleArrowLeft, contentDescription = "Back")
                }
            }
        }

        panes(
            state = paneState,
            left = {
                val tree = TreeGroup(editor.rootDirectory.path.name, removed.toTree {
                    editor.keys += it
                    removed -= it
                }, false)

                tree.content()
            },
            right = {
                val tree = TreeGroup(editor.rootDirectory.path.name, editor.keys.toTree {
                    editor.keys -= it
                    removed += it
                }, false)

                tree.content()
            },
        )
    }
}