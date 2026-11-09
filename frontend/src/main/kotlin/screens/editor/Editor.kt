package me.flamingo.pckd.frontend.screens.editor

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DataObject
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import me.flamingo.pckd.backend.ResourceGroup
import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.config.programs.Programs
import me.flamingo.pckd.backend.key.Key
import me.flamingo.pckd.frontend.components.*
import me.flamingo.pckd.frontend.components.tree.TreeEntry
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
    fun TopBar() = Row(Modifier.fillMaxWidth().padding(vertical = 5.dp).height(40.dp), verticalAlignment = Alignment.CenterVertically) {
        
        @Composable
        fun Button(name: String, icon: ImageVector, onClick: () -> Unit) {
            Tooltip(name) {
                SmallButton(
                    onClick, 
                    Modifier.size(40.dp).aspectRatio(1f),
                ) {
                    Icon(icon, it, Modifier.size(25.dp))
                }
            }
        }

        Spacer(Modifier.width(5.dp))
        Button("Back", Icons.Rounded.KeyboardDoubleArrowLeft, ::previous)
        Spacer(Modifier.width(7.dp))
        Button("Reload", Icons.Rounded.Refresh, ::reload)
        Spacer(Modifier.width(7.dp))
        Button("Modify Groups", Icons.Rounded.Edit) { 
            next { GroupEditor(this@Editor) } 
        }
        Spacer(Modifier.width(7.dp))
        Button("Groups (JSON)", Icons.Rounded.DataObject) { 
            Programs.File.value.textProgram.run(rootDirectory.groups.path, rootDirectory) 
        }
    }
    
    @Composable
    fun Pane(content: @Composable () -> Unit) = Surface(
        color = Mocha.mantle,
        modifier = Modifier.fillMaxSize().padding(5.dp),
        shape = Theme.Shapes.small
    ) {
        ScrollPane(Modifier.padding(10.dp)) {
            content()
        }
    }
    
    @Composable
    fun SelectorPane() = Pane {
        var selectedEntry by remember { mutableStateOf<TreeEntry?>(null) }
        
        TreeGroup(
            rootDirectory.path.name,
            keys.toTree {
                selectedEntry?.isSelected = false
                isSelected = true
                selectedEntry = this
                selected = it 
            },
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
    override fun Content() = Box() {
        Column(Modifier.fillMaxSize().padding(6.dp)) {
            TopBar()

            Panes(
                state = paneState,
                left  = ::SelectorPane,
                right = ::EditorPane,
            )
        }
    }
}
