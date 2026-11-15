package me.flamingo.pckd.frontend.screens.editor.groupeditor

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.flamingo.pckd.frontend.components.tree.TreeGroup
import me.flamingo.pckd.frontend.components.tree.toTree
import me.flamingo.pckd.frontend.screens.Screen
import me.flamingo.pckd.frontend.screens.editor.Editor
import me.flamingo.pckd.frontend.theming.Mocha
import kotlin.io.path.name

class GroupEditor(val editor: Editor) : Screen {
    
    
    fun getAll() {
        editor.rootDirectory
    }
    
    fun reload(): GroupEditor {
        Screen.back()
        return editor.reload().openGroupEditor()
    }
    
    @Composable
    fun TopBar() = Row(Modifier.fillMaxWidth().padding(vertical = 5.dp).height(40.dp), verticalAlignment = Alignment.CenterVertically) {
        
        Spacer(Modifier.width(5.dp))
        editor.TopBarButton("Back", Icons.Rounded.KeyboardDoubleArrowLeft, Screen::back)
        Spacer(Modifier.width(7.dp))
        editor.TopBarButton("Reload", Icons.Rounded.Refresh, ::reload)
    }
    
    @Composable
    fun CurrentsPane() = editor.EditorPane {
        TreeGroup(
            editor.rootDirectory.name,
            mergeable = false,
            children = editor.keys.toTree(),
        ).content()
    }
    
    @Composable
    override fun Content() = Surface(color = Mocha.crust) {
        Column(Modifier.fillMaxSize().padding(5.5.dp)) {
            TopBar()

            CurrentsPane()
        }
    }
}