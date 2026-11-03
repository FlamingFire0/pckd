package me.flamingo.pckd.frontend.screens.editor

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.FileCopy
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.flamingo.pckd.backend.ResourceGroup
import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.config.programs.Programs
import me.flamingo.pckd.backend.key.Key
import me.flamingo.pckd.frontend.Mocha
import me.flamingo.pckd.frontend.common.ObservableList
import me.flamingo.pckd.frontend.common.PanesState
import me.flamingo.pckd.frontend.common.panes
import me.flamingo.pckd.frontend.common.scrollPane
import me.flamingo.pckd.frontend.common.tree.TreeGroup
import me.flamingo.pckd.frontend.common.tree.toTree
import me.flamingo.pckd.frontend.screens.Home
import me.flamingo.pckd.frontend.screens.Screen
import me.flamingo.pckd.frontend.screens.editor.group_editor.GroupEditor
import me.flamingo.pckd.frontend.screens.editor.groups.groupResourceView
import me.flamingo.pckd.frontend.shapes
import kotlin.io.path.name

class Editor(val rootDirectory: RootDirectory) : Screen(Home) {
    
    companion object {
        private val paneState = PanesState()
    }
    
    var selected: Key? by mutableStateOf(null)
    val keys = ObservableList(rootDirectory.groups.value.groups)
    
    fun reload() {
        val newEditor = Editor(RootDirectory(rootDirectory.path))
        newEditor.selected = selected
        newEditor.open()
    }
    
    @Composable
    fun topBar() = Row(Modifier.fillMaxWidth().height(40.dp)) {
        IconButton(::previous) {
            Icon(imageVector = Icons.Rounded.KeyboardDoubleArrowLeft, contentDescription = "Back")
        }

        IconButton(::reload) {
            Icon(imageVector = Icons.Rounded.Refresh, contentDescription = "Reload")
        }

        IconButton({ Programs.File.value.textProgram.run(rootDirectory.groups.path, rootDirectory) }) {
            Icon(imageVector = Icons.Rounded.FileCopy, contentDescription = "Groups")
        }

        IconButton({ next { GroupEditor(this@Editor) } }) {
            Icon(imageVector = Icons.Rounded.AddCircle, contentDescription = "Add")
        }
    }
    
    @Composable
    fun pane(content: @Composable () -> Unit) = Surface(
        color = Mocha.mantle,
        modifier = Modifier.fillMaxSize().padding(5.dp),
        shape = shapes.medium
    ) {
        scrollPane(Modifier.padding(10.dp)) {
            content()
        }
    }
    
    @Composable
    fun selectorPane() = pane {
        TreeGroup(
            rootDirectory.path.name,
            keys.toTree { selected = it },
            false
        ).content()
    }
    
    @Composable
    fun editorPane() = pane {
        Row(Modifier.padding(20.dp)) {
            val group = ResourceGroup(rootDirectory, selected ?: return@Row)

            groupResourceView(group.textures, "Texture")
            Spacer(Modifier.width(20.dp))
            groupResourceView(group.models, "Model")
            Spacer(Modifier.width(20.dp))
            groupResourceView(group.items, "Item")
        }
    }

    @Composable
    override fun content() = Surface(color = Mocha.crust) {
        Column(Modifier.fillMaxSize()) {
            topBar()

            panes(
                state = paneState,
                left  = ::selectorPane,
                right = ::editorPane,
            )
        }
    }
}
