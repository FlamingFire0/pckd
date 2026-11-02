package me.flamingo.pckd.frontend.screens.editor

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.FileCopy
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.flamingo.pckd.backend.ResourceGroup
import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.config.programs.Programs
import me.flamingo.pckd.backend.key.Key
import me.flamingo.pckd.frontend.Mocha
import me.flamingo.pckd.frontend.common.ObservableList
import me.flamingo.pckd.frontend.common.PanesState
import me.flamingo.pckd.frontend.common.panes
import me.flamingo.pckd.frontend.common.scrollPane
import me.flamingo.pckd.frontend.common.tree.TreeGroup
import me.flamingo.pckd.frontend.common.tree.toTree
import me.flamingo.pckd.frontend.screens.Home
import me.flamingo.pckd.frontend.screens.Screen
import me.flamingo.pckd.frontend.screens.editor.group_editor.GroupEditor
import me.flamingo.pckd.frontend.screens.editor.groups.groupResourceView
import kotlin.io.path.name

class Editor(val rootDirectory: RootDirectory) : Screen(Home) {
    
    companion object {
        private val paneState = PanesState()
    }
    
    var selected: Key? by mutableStateOf(null)
    val keys = ObservableList(rootDirectory.groups.value.groups)
    
    fun reload() = Editor(RootDirectory(rootDirectory.path)).open()
    
    @Composable
    override fun content() = Column(Modifier.fillMaxSize()) {
        Surface(color = Mocha.crust) {
            Row(Modifier.fillMaxWidth()) {
                IconButton(::previous) {
                    Icon(imageVector = Icons.Rounded.KeyboardDoubleArrowLeft, contentDescription = "Back")
                }

                IconButton(::reload) {
                    Icon(imageVector = Icons.Rounded.Refresh, contentDescription = "Reload")
                }

                IconButton({ Programs.File.value.textProgram.run(rootDirectory.groups.path, rootDirectory) }) {
                    Icon(imageVector = Icons.Rounded.FileCopy, contentDescription = "Groups")
                }
                    
                IconButton({ next { GroupEditor(this@Editor) } }) { 
                    Icon(imageVector = Icons.Rounded.AddCircle, contentDescription = "Add") 
                } 
            } 
        }

        panes(
            state = paneState,
            left = {
                Surface(
                    color = Mocha.crust,
                    modifier = Modifier.fillMaxSize()
                ) {
                    scrollPane {
                        TreeGroup(
                            rootDirectory.path.name,
                            keys.toTree { selected = it },
                            false,).content()
                    }
                }
            },
                
            right = {
                Row {
                    if(selected == null) return@panes
                    val group = ResourceGroup(rootDirectory, selected!!)

                    Spacer(Modifier.width(20.dp))
                        
                    val textures = ObservableList(group.textures.resources)
                    val models   = ObservableList(group.models  .resources)
                    val items    = ObservableList(group.items   .resources)

                    groupResourceView("Texture", resources = textures) {
                        group.textures.add(it)
                        textures.invalidate()
                    }
                        
                    Spacer(Modifier.width(20.dp))
                        
                    groupResourceView("Model", resources = models) {
                        group.models.add(it)
                        models.invalidate()
                    }
                        
                    Spacer(Modifier.width(20.dp))
                        
                    groupResourceView("Item", resources = items) {
                        group.items.add(it)
                        items.invalidate()
                    }
                }
            }
        )
    }
}