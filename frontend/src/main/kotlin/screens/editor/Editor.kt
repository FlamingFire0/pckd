package me.flamingo.pckd.frontend.screens.editor

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.flamingo.pckd.backend.ResourceGroup
import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.key.Key
import me.flamingo.pckd.frontend.App
import me.flamingo.pckd.frontend.Mocha
import me.flamingo.pckd.frontend.common.panes
import me.flamingo.pckd.frontend.common.tree.TreeGroup
import me.flamingo.pckd.frontend.common.tree.toTree
import me.flamingo.pckd.frontend.screens.Screen
import me.flamingo.pckd.frontend.screens.editor.groups.groupResourceView
import kotlin.io.path.name

class Editor(val rootDirectory: RootDirectory) : Screen {
    
    fun reload() {
        App.screen = Editor(RootDirectory(rootDirectory.path))
    }
    
    @Composable
    override fun content() {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            editorTopBar(this@Editor)

            val keys = remember {
                mutableStateListOf<Key>().apply {
                    addAll(rootDirectory.groups.value.groups)
                }
            }
            
            var selected: Key? by remember { mutableStateOf(null) }

            panes(
                left = {
                    Surface(
                        color = Mocha.crust,
                        modifier = Modifier.fillMaxSize()
                    ) {

                        TreeGroup(
                            rootDirectory.path.name,
                            keys.toTree { selected = it },
                            false,
                        ).content()
                    }
                },
                
                right = {
                    Row {
                        if(selected == null) return@panes
                        val group = ResourceGroup(rootDirectory, selected!!)

                        Spacer(Modifier.width(20.dp))

                        groupResourceView("Textures", group.textures)
                        Spacer(Modifier.width(20.dp))
                        groupResourceView("Models", group.models)
                        Spacer(Modifier.width(20.dp))
                        groupResourceView("Items", group.items)
                        
                    }
                }
            )
        }
    }
}