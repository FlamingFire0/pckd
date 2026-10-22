package me.flamingo.pckd.frontend.screens.editor

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.SubdirectoryArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.flamingo.pckd.frontend.App
import me.flamingo.pckd.frontend.Mocha
import me.flamingo.pckd.frontend.screens.Home

@Composable
fun editorTopBar(editor: Editor) {
    Surface(
        color = Mocha.crust
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {

            IconButton(onClick = { App.screen = Home }) {
                Icon(imageVector = Icons.Rounded.SubdirectoryArrowLeft, contentDescription = "Back")
            }

            IconButton(onClick = { editor.reload() }) {
                Icon(imageVector = Icons.Rounded.Refresh, contentDescription = "Reload")
            }
        }
    }
    
}