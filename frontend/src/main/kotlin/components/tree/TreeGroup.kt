package me.flamingo.pckd.frontend.components.tree

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.FolderOpen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import me.flamingo.pckd.frontend.components.FocusBox
import me.flamingo.pckd.frontend.components.FocusBox.focusManager
import me.flamingo.pckd.frontend.components.RowButton
import me.flamingo.pckd.frontend.theming.Mocha
import me.flamingo.pckd.frontend.theming.Theme

class TreeGroup(
    val name: String,
    val children: List<TreeElement> = emptyList(),
    val mergeable: Boolean = true,
) : TreeElement {
    
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

        Column(Modifier.fillMaxWidth()) {
            FocusBox(
                left = {
                    if(expanded) expanded = false
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
                        else          Icons.Rounded.Folder,
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

                    when (event.key) {
                        
                        Key.DirectionDown -> focusManager.moveFocus(FocusDirection.Down)
                        Key.DirectionUp   -> focusManager.moveFocus(FocusDirection.Up)
                        else -> false
                    }
                }
            )

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