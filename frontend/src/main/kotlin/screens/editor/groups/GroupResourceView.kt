package me.flamingo.pckd.frontend.screens.editor.groups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.flamingo.pckd.backend.resources.ModelResource
import me.flamingo.pckd.backend.resources.Resource
import me.flamingo.pckd.backend.resources.TextureResource
import me.flamingo.pckd.frontend.Mocha
import me.flamingo.pckd.frontend.common.context_menu.ContextAction
import me.flamingo.pckd.frontend.common.context_menu.contextMenu
import me.flamingo.pckd.frontend.common.grouping
import me.flamingo.pckd.frontend.common.openInFileManager
import me.flamingo.pckd.frontend.common.popup
import me.flamingo.pckd.frontend.common.rowButton
import me.flamingo.pckd.frontend.jetbrainsMono
import kotlin.math.roundToInt

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun groupResourceView(
    name: String,
    plural: String = name + "s",
    resources: List<Resource<*, *>>,
    onAdd: (String) -> Unit = {},
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(if(resources.size < 2) name else plural)

        grouping {
            resources.forEach {
                if(!it.getFile().exists()) return@forEach

                contextMenu(
                    listOfNotNull(
                        ContextAction("Open Parent Directory") {
                            it.getFile().path.parent.openInFileManager()
                        },
                        
                        if(it is ModelResource  ) ContextAction("Open as Text" ) { it.openText()     } else null,
                        if(it is TextureResource) ContextAction("Open Meta"    ) { it.openMeta()     } else null,
                        if(it is TextureResource) ContextAction("Open Aseprite") { it.openAseprite() } else null,
                    )
                ) {
                    rowButton(onClick = { it.open() }, horizontalArrangement = Arrangement.Center) {
                        Text(it.key.toString(true))
                    }
                }
            }

            val showPopup = remember { mutableStateOf(false) }
            var popupPosition by remember { mutableStateOf(IntOffset.Zero) }
            

            popup(showPopup, popupPosition) {
                var text by remember { mutableStateOf("") }
                
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("New $name")


                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = { Text("Resource name") },
                        singleLine = true,
                        shape = RoundedCornerShape(6.dp),
                        textStyle = TextStyle(fontSize = 14.sp, fontFamily = jetbrainsMono),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Mocha.text,
                            cursorColor = Mocha.red,
                            backgroundColor = Mocha.base,
                            focusedBorderColor = Mocha.red,
                            unfocusedBorderColor = Mocha.surface0,
                            placeholderColor = Mocha.overlay0,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { text = "" }) {
                                Icon(imageVector = Icons.Rounded.Close, contentDescription = "Clear")
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = { 
                                showPopup.value = false
                                onAdd(text)
                            }
                        )
                    )
                }
            }
            
            
            
            rowButton(
                onClick = { showPopup.value = true }, 
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.onPointerEvent(PointerEventType.Press) {
                    val p = it.changes.first().position
                    popupPosition = IntOffset(p.x.roundToInt(), p.y.roundToInt())
                }
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add")
            }
        }
    }
}