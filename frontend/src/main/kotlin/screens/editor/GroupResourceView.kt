package me.flamingo.pckd.frontend.screens.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.flamingo.pckd.backend.ResourceSlot
import me.flamingo.pckd.backend.resources.ItemResource
import me.flamingo.pckd.backend.resources.ModelResource
import me.flamingo.pckd.backend.resources.Resource
import me.flamingo.pckd.backend.resources.TextureResource
import me.flamingo.pckd.frontend.components.Grouping
import me.flamingo.pckd.frontend.components.Popup
import me.flamingo.pckd.frontend.components.PopupState
import me.flamingo.pckd.frontend.components.RowButton
import me.flamingo.pckd.frontend.components.contextmenu.ContextAction
import me.flamingo.pckd.frontend.components.contextmenu.ContextMenu
import me.flamingo.pckd.frontend.theming.Theme
import me.flamingo.pckd.frontend.utilities.ObservableList
import me.flamingo.pckd.frontend.utilities.platform.openInFileManager


object GroupResourceView {
    
    @Composable
    fun Textures(textures: ResourceSlot<TextureResource, *>) {
        Base(textures, "Texture") {
            add(ContextAction("Open Meta") { it.openMeta() })
            add(ContextAction("Open Aseprite") { it.openAseprite() })
        }
    }

    @Composable
    fun Models(textures: ResourceSlot<ModelResource, *>) {
        Base(textures, "Model") {
            add(ContextAction("Open as Text") { it.openText() })
        }
    }

    @Composable
    fun Items(textures: ResourceSlot<ItemResource, *>) {
        Base(textures, "Item")
    }

    @Composable
    private fun Title(resources: Int, name: String, plural: String = "s") {
        Text(if(resources < 2) name else plural)
    }
    
    @Composable
    private fun <R: Resource<R, *>> Base(
        resourceSlot: ResourceSlot<R, *>,
        name: String,
        plural: String = name + "s",
        contextActions: MutableList<ContextAction>.(R) -> Unit = {}
    ) {
        val resources = ObservableList(resourceSlot.resources)
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Title(resources.size, name, plural)

            Grouping {
                resources.forEach {
                    if (!it.getFile().exists()) return@forEach

                    ContextMenu(
                        mutableListOf(
                            ContextAction("Open Parent Directory") {
                                it.getFile().path.parent.openInFileManager()
                            },
                        ).apply { contextActions(it) }
                    ) {
                        RowButton({ it.open() }, horizontalArrangement = Arrangement.Center) {
                            Text(it.key.toString(true))
                        }
                    }
                }

                val popupState = remember { PopupState() }

                Popup(popupState) {
                    var text by remember { mutableStateOf("") }
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("New $name")

                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            placeholder = { Text("Resource name") },
                            singleLine = true,
                            shape = RoundedCornerShape(6.dp),
                            textStyle = TextStyle(fontSize = 14.sp, fontFamily = Theme.fontFamily),
                            colors = Theme.textFieldColors,
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                IconButton(onClick = { text = "" }) {
                                    Icon(imageVector = Icons.Rounded.Close, contentDescription = "Clear")
                                }
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    it.visible = false
                                    resourceSlot.add(text)
                                    resources.invalidate()
                                }
                            )
                        )
                    }
                }

                @OptIn(ExperimentalComposeUiApi::class)
                RowButton(
                    onClick = popupState::show,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.onPointerEvent(PointerEventType.Press, onEvent = { popupState.updatePositionFromEvent(it) })
                ) {
                    Icon(Icons.Rounded.Add, contentDescription = "Add")
                }
            }
        }
    }
}