package me.flamingo.pckd.frontend.screens.editor

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DataObject
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import me.flamingo.pckd.backend.ResourceGroup
import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.config.programs.Programs
import me.flamingo.pckd.backend.key.Key
import me.flamingo.pckd.frontend.components.Panes
import me.flamingo.pckd.frontend.components.PanesState
import me.flamingo.pckd.frontend.components.ScrollPane
import me.flamingo.pckd.frontend.components.Tooltip
import me.flamingo.pckd.frontend.components.tree.TreeGroup
import me.flamingo.pckd.frontend.components.tree.toTree
import me.flamingo.pckd.frontend.screens.Home
import me.flamingo.pckd.frontend.screens.Screen
import me.flamingo.pckd.frontend.screens.editor.groupeditor.GroupEditor
import me.flamingo.pckd.frontend.theming.Mocha
import me.flamingo.pckd.frontend.theming.Theme
import me.flamingo.pckd.frontend.utilities.ObservableList
import kotlin.io.path.name

class Editor(val rootDirectory: RootDirectory) : Screen(Home) {
    
    companion object {
        private val paneState = PanesState(350.dp)
    }
    
    var selected: Key? by mutableStateOf(null)
    val keys = ObservableList(rootDirectory.groups.value.groups)
    
    fun reload() {
        val newEditor = Editor(RootDirectory(rootDirectory.path))
        newEditor.selected = selected
        newEditor.open()
    }
    
    @Composable
    fun TopBar() = Row(Modifier.fillMaxWidth().height(40.dp), verticalAlignment = Alignment.CenterVertically) {
        
        @Composable
        fun Button(name: String, icon: ImageVector, onClick: () -> Unit) {
            Tooltip(name) {
                IconButton(onClick, Modifier.size(35.dp)) {
                    Icon(icon, it, Modifier.size(25.dp))
                }
            }
        }

        Button("Back", Icons.Rounded.KeyboardDoubleArrowLeft, ::previous)
        
        Button("Reload", Icons.Rounded.Refresh, ::reload)
        
        Button("Modify Groups", Icons.Rounded.Edit) { 
            next { GroupEditor(this@Editor) } 
        }
        
        Button("Groups (JSON)", Icons.Rounded.DataObject) { 
            Programs.File.value.textProgram.run(rootDirectory.groups.path, rootDirectory) 
        }
    }
    
    @Composable
    fun Pane(content: @Composable () -> Unit) = Surface(
        color = Mocha.mantle,
        modifier = Modifier.fillMaxSize().padding(5.dp),
        shape = Theme.shapes.medium
    ) {
        ScrollPane(Modifier.padding(10.dp)) {
            content()
        }
    }
    
    @Composable
    fun SelectorPane() = Pane {
        TreeGroup(
            rootDirectory.path.name,
            keys.toTree { selected = it },
            false
        ).content()
    }
    
    @Composable
    fun EditorPane() = Pane {
        Row(Modifier.padding(20.dp)) {
            val group = ResourceGroup(rootDirectory, selected ?: return@Row)

            GroupResourceView.Textures(group.textures)
            Spacer(Modifier.width(20.dp))
            GroupResourceView.Models(group.models)
            Spacer(Modifier.width(20.dp))
            GroupResourceView.Items(group.items)
        }
    }

    @Composable
    override fun content() = Surface(color = Mocha.crust) {
        Column(Modifier.fillMaxSize()) {
            TopBar()

            Panes(
                modifier = Modifier.padding(horizontal = 6.dp).padding(bottom = 6.dp),
                state = paneState,
                left  = ::SelectorPane,
                right = ::EditorPane,
            )
        }
    }
}
