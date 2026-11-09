package me.flamingo.pckd.frontend.components.tree

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import me.flamingo.pckd.frontend.components.FocusBox
import me.flamingo.pckd.frontend.components.RowButton
import me.flamingo.pckd.frontend.theming.Theme

class TreeEntry(
    val name: String,
    val onClick: () -> Unit = {}
) : TreeElement {

    override fun name(): String = name

    @Composable
    override fun content(depth: Int) {
        FocusBox(
            left  = FocusBox::moveUp,
            right = FocusBox::moveDown,
        ) {
            RowButton(onClick, Modifier.clip(Theme.shapes.medium)) {
                Spacer(Modifier.width((depth * 20).dp))

                Icon(Icons.Rounded.Apps, "Element")

                Spacer(Modifier.width(6.dp))

                Text(name)
            }   
        }
    }
}