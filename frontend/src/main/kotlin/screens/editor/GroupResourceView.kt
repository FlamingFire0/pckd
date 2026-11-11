package me.flamingo.pckd.frontend.screens.editor

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.flamingo.pckd.backend.ResourceSlot
import me.flamingo.pckd.backend.resources.ItemResource
import me.flamingo.pckd.backend.resources.ModelResource
import me.flamingo.pckd.backend.resources.Resource
import me.flamingo.pckd.backend.resources.TextureResource
import me.flamingo.pckd.frontend.components.ConfirmationDialog
import me.flamingo.pckd.frontend.components.Grouping
import me.flamingo.pckd.frontend.components.RowButton
import me.flamingo.pckd.frontend.components.Tooltip
import me.flamingo.pckd.frontend.components.contextmenu.ContextAction
import me.flamingo.pckd.frontend.components.contextmenu.ContextMenu
import me.flamingo.pckd.frontend.components.popup.Popup
import me.flamingo.pckd.frontend.components.popup.noDragPopup
import me.flamingo.pckd.frontend.theming.Mocha
import me.flamingo.pckd.frontend.theming.Theme
import me.flamingo.pckd.frontend.utilities.ErrorHandler
import me.flamingo.pckd.frontend.utilities.ObservableList
import me.flamingo.pckd.frontend.utilities.platform.openInFileManager
import java.nio.file.Path
import javax.imageio.ImageIO
import kotlin.math.min


object GroupResourceView {

    private fun loadPreview(path: Path): ImageBitmap? = runCatching {
        val image = ImageIO.read(path.toFile()) ?: return@runCatching null

        // Animated textures are vertical strips of square frames; show the first one.
        val frameSize = min(image.width, image.height)
        image.getSubimage(0, 0, image.width, frameSize).toComposeImageBitmap()
    }.getOrNull()

    @Composable
    fun getBitmap(texture: TextureResource): ImageBitmap? {
        val path = texture.getFile().path
        val modified = path.toFile().lastModified()

        return remember(path, modified) { loadPreview(path) }
    }
    
    @Composable
    fun Textures(textures: ResourceSlot<TextureResource, *>) {
        Base(
            textures,
            "Texture",
            button = {
                val bitmap = getBitmap(this)
                
                Tooltip(
                    content =  {
                        if(bitmap == null) return@Tooltip
                        
                        Column(Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Preview")
                            
                            Image(
                                bitmap = bitmap,
                                contentDescription = null,
                                filterQuality = FilterQuality.None,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.requiredSize(128.dp)
                            )
                        }
                    },
                    enabled = bitmap != null,
                ) {
                    Row {
                        if (bitmap != null) {
                            Box(
                                modifier = Modifier.size(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    bitmap = bitmap,
                                    contentDescription = null,
                                    filterQuality = FilterQuality.None,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.requiredSize(32.dp).align(Alignment.Center)
                                )
                            }

                            Spacer(Modifier.width(10.dp))
                        }

                        it()
                    }
                }
            },
        ) {
            if (it.getMeta().exists())
                add(ContextAction("Open Meta File") { it.openMeta() })
            
            if (it.getAseprite().exists())
                add(ContextAction("Open Aseprite File") { it.openAseprite() })
        }
    }

    @Composable
    fun Models(textures: ResourceSlot<ModelResource, *>) {
        Base(textures, "Model") {
            add(ContextAction("Open as Text File") { it.openText() })
        }
    }

    @Composable
    fun Items(textures: ResourceSlot<ItemResource, *>) {
        Base(textures, "Item")
    }

    @Composable
    private fun Title(resources: Int, name: String, plural: String = "s") {
        Text(if (resources < 2) name else plural)
    }
    
    @Composable
    private fun <R: Resource<R, *>> Base(
        resourceSlot: ResourceSlot<R, *>,
        name: String,
        plural: String = name + "s",
        button: @Composable R.(@Composable R.() -> Unit) -> Unit = { it() },
        additionalResourceContent: @Composable (R) -> Unit = {},
        contextActions: MutableList<ContextAction>.(R) -> Unit = {}
    ) {
        val resources = ObservableList(resourceSlot.resources)
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Title(resources.size, name, plural)

            Grouping {
                resources.forEach {
                    if (!it.getFile().exists()) return@forEach

                    val deletionPopup = ConfirmationDialog(
                        """
                            Are you sure you want to 
                            delete ${name.lowercase()} "${it.key.getLeaf()}"?
                        """.trimIndent(),
                        "Delete $name"
                    ) {
                        ErrorHandler.error("meow")
                    }
                    
                    ContextMenu(
                        mutableListOf(
                            ContextAction("Open Parent Directory") {
                                it.getFile().path.parent.openInFileManager()
                            },
                            ContextAction("Delete") {
                                deletionPopup.show()
                            }
                            
                        ).apply { contextActions(it) },
                        content = {
                            RowButton(
                                { it.open() },
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.height(IntrinsicSize.Max)
                            ) {
                                it.button { Text(key.toString(true)) }
                            }
                        }
                    )

                    additionalResourceContent(it)
                }
                
                val creationPopup = Popup(Modifier.padding(10.dp), draggable = true) {
                    var text by remember { mutableStateOf(if (resourceSlot.isEmpty()) resourceSlot.key.getLeaf() else "") }

                    fun add() {
                        it.visible = false
                        resourceSlot.add(text)
                        resources.invalidate()
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("New $name", Modifier.noDragPopup())

                        Spacer(Modifier.height(10.dp))

                        OutlinedTextField(
                            value = text,
                            onValueChange = { value -> text = value },
                            placeholder = { Text("Resource name") },
                            singleLine = true,
                            shape = RoundedCornerShape(6.dp),
                            textStyle = TextStyle(fontSize = 14.sp, fontFamily = Theme.fontFamily),
                            colors = Theme.textFieldColors(Mocha.green),
                            modifier = Modifier.fillMaxWidth().noDragPopup(),
                            trailingIcon = {
                                IconButton(onClick = { text = "" }) {
                                    Icon(
                                        imageVector = Icons.Rounded.Close,
                                        contentDescription = "Clear",
                                        tint = Mocha.overlay2
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = { add() }
                            )
                        )

                        Spacer(Modifier.height(10.dp))

                        Button(
                            ::add,
                            Modifier.noDragPopup(),
                            shape = Theme.Shapes.small,
                            colors = ButtonDefaults.textButtonColors(Mocha.green, Mocha.crust)
                        ) {
                            Text("Create")
                        }
                    }
                }

                @OptIn(ExperimentalComposeUiApi::class)
                RowButton(
                    onClick = creationPopup::show,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.onPointerEvent(PointerEventType.Press, onEvent = { creationPopup.updatePositionFromEvent(it) })
                ) {
                    Icon(Icons.Rounded.Add, "Add", tint = Mocha.green)
                }
            }
        }
    }
}