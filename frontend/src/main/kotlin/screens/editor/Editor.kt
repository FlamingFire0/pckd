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
import me.flamingo.pckd.backend.key.Key
import me.flamingo.pckd.frontend.components.Panes
import me.flamingo.pckd.frontend.components.PanesState
import me.flamingo.pckd.frontend.components.ScrollPane
import me.flamingo.pckd.frontend.components.SmallButton
import me.flamingo.pckd.frontend.components.tooltip.Tooltip
import me.flamingo.pckd.frontend.components.tree.TreeEntry
import me.flamingo.pckd.frontend.components.tree.TreeGroup
import me.flamingo.pckd.frontend.components.tree.toTree
import me.flamingo.pckd.frontend.screens.Screen
import me.flamingo.pckd.frontend.screens.editor.groupeditor.GroupEditor
import me.flamingo.pckd.frontend.theming.Mocha
import me.flamingo.pckd.frontend.theming.Theme
import me.flamingo.pckd.frontend.utilities.ObservableList

class Editor(
    val rootDirectory: RootDirectory,
    selected: Key? = null
) : Screen {
    
    companion object {
        val paneState = PanesState(350.dp)
    }
    
    val keys = ObservableList(rootDirectory.groups)
    var selected: Key? by mutableStateOf(selected.takeIf { it in keys })
    
    val treeRoot: TreeGroup = TreeGroup(rootDirectory.name, mergeable = false)
    
    fun reload(): Editor {
        val newEditor = Editor(RootDirectory(rootDirectory.path), selected)
        Screen.replace(newEditor)
        return newEditor
    }
    
    fun openGroupEditor() = GroupEditor(this).also { Screen.open(it) }

    @Composable
    fun TopBarButton(name: String, icon: ImageVector, onClick: () -> Unit) {
        Tooltip(name) {
            SmallButton(
                onClick,
                Modifier.size(40.dp).aspectRatio(1f),
            ) {
                Icon(icon, it, Modifier.size(25.dp))
            }
        }
    }
    
    @Composable
    fun TopBar() = Row(Modifier.fillMaxWidth().padding(vertical = 5.dp).height(40.dp), verticalAlignment = Alignment.CenterVertically) {

        Spacer(Modifier.width(5.dp))
        TopBarButton("Back", Icons.Rounded.KeyboardDoubleArrowLeft, Screen::back)
        Spacer(Modifier.width(7.dp))
        TopBarButton("Reload", Icons.Rounded.Refresh, ::reload)
        Spacer(Modifier.width(7.dp))
        TopBarButton("Modify Groups", Icons.Rounded.Edit, ::openGroupEditor)
        Spacer(Modifier.width(7.dp))
        TopBarButton("Groups (JSON)", Icons.Rounded.DataObject) { 
            rootDirectory.groupsConfig.open(rootDirectory)
        }
    }
    
    @Composable
    fun EditorPane(content: @Composable () -> Unit) = Surface(
        color = Mocha.mantle,
        modifier = Modifier.fillMaxSize().padding(5.dp),
        shape = Theme.Shapes.small
    ) {
        ScrollPane(Modifier.padding(10.dp)) {
            content()
        }
    }
    
    @Composable
    fun SelectorPane() = EditorPane {
        val selectedEntry = remember { mutableStateOf<TreeEntry?>(null) }
        
        if(treeRoot.children.isEmpty())
            treeRoot.children.addAll(
                keys.toTree(selected, selectedEntry) {
                    selectedEntry.value?.isSelected = false
                    isSelected = true
                    selectedEntry.value = this
                    selected = it
                },
            )
        
        treeRoot.content()
    }
    
    @Composable
    fun EditorPane() = EditorPane {
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
        Column(Modifier.fillMaxSize().padding(5.5.dp)) {
            TopBar()

            Panes(
                state = paneState,
                left  = ::SelectorPane,
                right = ::EditorPane,
            )
        }
    }
}
