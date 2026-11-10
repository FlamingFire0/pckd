package me.flamingo.pckd.frontend.components.tree

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.FolderOpen
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import me.flamingo.pckd.frontend.components.FocusBox
import me.flamingo.pckd.frontend.components.FocusBox.focusManager
import me.flamingo.pckd.frontend.components.RowButton
import me.flamingo.pckd.frontend.components.contextmenu.ContextAction
import me.flamingo.pckd.frontend.components.contextmenu.ContextMenu
import me.flamingo.pckd.frontend.theming.Mocha
import me.flamingo.pckd.frontend.theming.Theme

class TreeGroup(
    val name: String,
    override var parent: TreeGroup? = null,
    val contextActions: (TreeGroup) -> List<ContextAction> = { listOf() },
    val mergeable: Boolean = true, 
    children: List<TreeElement> = emptyList(),
) : TreeElement {
    val children = mutableStateListOf<TreeElement>().also { it.addAll(children) }
    
    
    var expanded by mutableStateOf(true)
    
    override fun name(): String {
        ifMergeable { child ->
            return "$name/${child.name()}"
        }
        
        return name
    }
    
    @Composable
    private fun Elements(depth: Int) {
        ifMergeable { child ->
            child.Elements(depth)
            return
        }

        children.forEach { child ->
            child.content(depth + 1)
        }
    }

    @Composable
    override fun content(depth: Int) {
        
        val contextActions = remember { contextActions(this) }

        Column(Modifier.fillMaxWidth()) {
            ContextMenu(contextActions) {
                FocusBox(
                    left = {
                        if (expanded) expanded = false
                        else FocusBox.moveUp()
                        true
                    },
                    right = {
                        if (!expanded) expanded = true
                        else FocusBox.moveDown()
                        true
                    }
                ) {
                    RowButton({ expanded = !expanded }, Modifier.clip(Theme.Shapes.small)) {
                        Spacer(Modifier.width((depth * 20).dp))

                        Icon(
                            if (expanded) Icons.Rounded.FolderOpen
                            else Icons.Rounded.Folder,
                            "Group",
                            tint = Mocha.yellow
                        )

                        Spacer(Modifier.width(6.dp))

                        Text(name())
                    }
                }
                Box(
                    Modifier.onKeyEvent { event ->
                        if (event.type != KeyEventType.KeyDown) return@onKeyEvent false

                        when(event.key) {

                            Key.DirectionDown -> focusManager.moveFocus(FocusDirection.Down)
                            Key.DirectionUp   -> focusManager.moveFocus(FocusDirection.Up)
                            else              -> false
                        }
                    }
                )
            }
                
            Spacer(Modifier.height(2.dp))

            if (expanded) Elements(depth)
        }
    }
    
    private inline fun ifMergeable(block: (child: TreeGroup) -> Unit) {
        if(children.size == 1 && mergeable) {
            val child = children.first()
            
            if(child !is TreeGroup) return
            
            block(child)
        }
    }
}