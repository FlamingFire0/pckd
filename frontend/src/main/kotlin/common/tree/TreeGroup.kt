package me.flamingo.pckd.frontend.common.tree

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.FolderOpen
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class TreeGroup(
    val name: String,
    val children: List<TreeElement> = emptyList(),
    val mergeable: Boolean = true,
) : TreeElement {

    override fun name(): String {
        
        ifOnlyChildIsGroup { child ->
            return "$name/${child.name()}"
        }
        
        return name
    }
    
    @Composable
    fun elements(depth: Int) {
        ifOnlyChildIsGroup { child ->
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
    
    private inline fun ifOnlyChildIsGroup(block: (child: TreeGroup) -> Unit) {
        if(children.size == 1 && mergeable) {
            val child = children.first()
            
            if(child !is TreeGroup) return
            
            block(child)
        }
    }
}