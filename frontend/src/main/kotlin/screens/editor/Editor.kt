package me.flamingo.pckd.frontend.screens.editor

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import me.flamingo.pckd.backend.ResourceGroup
import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.key.Key
import me.flamingo.pckd.frontend.components.Panes
import me.flamingo.pckd.frontend.components.PanesState
import me.flamingo.pckd.frontend.components.ScrollPane
import me.flamingo.pckd.frontend.components.SmallButton
import me.flamingo.pckd.frontend.components.contextmenu.ContextAction
import me.flamingo.pckd.frontend.components.tooltip.Tooltip
import me.flamingo.pckd.frontend.components.tree.TreeEntry
import me.flamingo.pckd.frontend.components.tree.TreeGroup
import me.flamingo.pckd.frontend.components.tree.toTree
import me.flamingo.pckd.frontend.config.Configs
import me.flamingo.pckd.frontend.config.Preferences
import me.flamingo.pckd.frontend.screens.Screen
import me.flamingo.pckd.frontend.screens.editor.groupeditor.GroupEditor
import me.flamingo.pckd.frontend.theming.Mocha
import me.flamingo.pckd.frontend.theming.Theme
import me.flamingo.pckd.frontend.utilities.ComposeStateFlow
import me.flamingo.pckd.frontend.utilities.DefaultScope
import me.flamingo.pckd.frontend.utilities.ObservableList

class Editor(
    val rootDirectory: RootDirectory,
    selected: Key? = null
) : Screen {

    @Serializable
    class EditorPreferences(
        val panesState: PanesState = PanesState(350.dp)
    )

    override fun onBack() {
        rootDirectory.groupsConfig.save()
        Preferences.save()
    }
    
    val keys = ObservableList(rootDirectory.groups)
    var selected: Key? by mutableStateOf(selected.takeIf { it in keys })
    var selectedEntry = mutableStateOf<TreeEntry?>(null)

    var treeRoot: TreeGroup? by ComposeStateFlow(null)

    init {
        DefaultScope.launch(Dispatchers.Default) {
            treeRoot = loadTree()
        }
    }

    fun loadTree(): TreeGroup =
        TreeGroup(
            rootDirectory.name,
            mergeable = false,
            children = keys.toTree(
                { it == selected },
                selectedEntry,
                {
                    add(ContextAction("Remove") {
                        removeKey(it.key)
                    })
                }
            ) {
                selectedEntry.value?.isSelected = false
                isSelected = true
                selectedEntry.value = this
                selected = it
            }
        )

    fun addKey(key: Key) {
        keys.add(key)
        updateKeys()
    }

    fun removeKey(key: Key) {
        keys.remove(key)
        updateKeys()
    }

    private fun updateKeys() {
        rootDirectory.groupsConfig.save()

        keys.invalidate()
        treeRoot = loadTree()
    }
    
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
        treeRoot?.content()
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
                state = Configs.preferences.editor.panesState,
                left  = ::SelectorPane,
                right = ::EditorPane,
            )
        }
    }
}
