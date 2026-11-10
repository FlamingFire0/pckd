package me.flamingo.pckd.frontend.components.tree

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.flamingo.pckd.backend.key.Key
import me.flamingo.pckd.frontend.components.FocusBox
import me.flamingo.pckd.frontend.components.RowButton
import me.flamingo.pckd.frontend.components.contextmenu.ContextMenu
import me.flamingo.pckd.frontend.resources.Res
import me.flamingo.pckd.frontend.resources.cube
import me.flamingo.pckd.frontend.theming.Mocha
import me.flamingo.pckd.frontend.theming.Theme
import org.jetbrains.compose.resources.painterResource

class TreeEntry(
    val name: String,
    val key: Key,
    override var parent: TreeGroup?,
    val onClick: (TreeEntry) -> Unit = {}, 
) : TreeElement {
    var isSelected by mutableStateOf(false)

    override fun name(): String = name

    @Composable
    override fun content(depth: Int) {
        val color = if(isSelected) Mocha.blue else Color.Unspecified
        val backgroundColor = if(isSelected) Mocha.surface0 else Color.Transparent
        
        
        
        ContextMenu(listOf(
            
        )) {
            FocusBox(
                left  = FocusBox::moveUp,
                right = FocusBox::moveDown,
            ) {
                Surface(Modifier.clip(Theme.Shapes.small), color = backgroundColor) {
                    RowButton({ onClick(this@TreeEntry) }, Modifier.clip(Theme.Shapes.small)) {
                        Spacer(Modifier.width((depth * 20).dp))

                        Icon(painterResource(Res.drawable.cube), "Element", tint = color)

                        Spacer(Modifier.width(6.dp))

                        Text(name, color = color)
                    }
                }
            }
        }
        
        Spacer(Modifier.height(2.dp))
    }
}