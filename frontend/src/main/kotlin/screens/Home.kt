package me.flamingo.pckd.frontend.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.dialogs.compose.rememberDirectoryPickerLauncher
import io.github.vinceglb.filekit.path
import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.config.History
import me.flamingo.pckd.frontend.App
import me.flamingo.pckd.frontend.common.context_menu.ContextAction
import me.flamingo.pckd.frontend.common.context_menu.contextMenu
import me.flamingo.pckd.frontend.common.grouping
import me.flamingo.pckd.frontend.common.rowButton
import me.flamingo.pckd.frontend.screens.editor.Editor
import java.nio.file.Path

object Home : Screen {
    
    fun selectDirectory(path: Path) {
        val root = RootDirectory(path)
        if(!root.isValid()) return
        
        History.File.value.history.remove(path)
        History.File.value.history.add(path)
        History.File.save()
        
        App.screen = Editor(root)
    }
    
    @Composable
    override fun content() {
        
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            val directoryPicker = rememberDirectoryPickerLauncher(
                onResult = { path ->
                    path?.path?.let {
                        selectDirectory(Path.of(it))
                    }
                }
            )

            Spacer(Modifier.height(20.dp))
            Button(onClick = { directoryPicker.launch() }) {
                Text("Open Pack") 
            }
            
            Text("Recent")

            grouping {
                History.File.value.history.reversed().forEach {

                    contextMenu(
                        items = listOf(
                            ContextAction("Rename") {
                                // Rename the selected resource
                            },
                            ContextAction("Delete") {
                                // Delete the selected resource
                            }
                        )
                        
                    ) {
                        rowButton(onClick = { selectDirectory(it) }) {
                            Text(it.toString())
                        }
                    }
                }
            }
        }
    }
}