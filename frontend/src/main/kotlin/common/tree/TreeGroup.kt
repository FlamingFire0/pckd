package me.flamingo.pckd.frontend.common.tree

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.FolderOpen
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.flamingo.pckd.frontend.common.rowButton

class TreeGroup(
    val name: String,
    val children: List<TreeElement> = emptyList(),
    val mergeable: Boolean = true,
) : TreeElement {

    override fun name(): String {
        ifMergeable { child ->
            return "$name/${child.name()}"
        }
        
        return name
    }
    
    @Composable
    fun elements(depth: Int) {
        ifMergeable { child ->
            child.elements(depth)
            return
        }

        children.forEach { child ->
            child.content(depth + 1)
        }
    }
    
    @Composable
    override fun content(depth: Int) {
        var expanded by remember { mutableStateOf(true) }

        Column(Modifier.fillMaxWidth()) {
            rowButton({ expanded = !expanded }) {
                Spacer(Modifier.width((depth * 20).dp))
                
                Icon(
                    if (expanded) Icons.Rounded.FolderOpen 
                    else          Icons.Rounded.Folder,
                    "Group",
                )
                
                Spacer(Modifier.width(6.dp))
                
                Text(name())
            }

            if (expanded) elements(depth)
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