package me.flamingo.pckd.frontend.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.vinceglb.filekit.dialogs.compose.rememberDirectoryPickerLauncher
import io.github.vinceglb.filekit.path
import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.config.History
import me.flamingo.pckd.common.Directories
import me.flamingo.pckd.frontend.components.Grouping
import me.flamingo.pckd.frontend.components.RowButton
import me.flamingo.pckd.frontend.components.SmallButton
import me.flamingo.pckd.frontend.components.contextmenu.ContextAction
import me.flamingo.pckd.frontend.components.contextmenu.ContextMenu
import me.flamingo.pckd.frontend.resources.Res
import me.flamingo.pckd.frontend.resources.pckd
import me.flamingo.pckd.frontend.screens.editor.Editor
import me.flamingo.pckd.frontend.theming.Mocha
import me.flamingo.pckd.frontend.theming.Theme
import me.flamingo.pckd.frontend.utilities.ObservableList
import me.flamingo.pckd.frontend.utilities.minify
import me.flamingo.pckd.frontend.utilities.platform.copyToClipboard
import me.flamingo.pckd.frontend.utilities.platform.openInFileManager
import org.jetbrains.compose.resources.painterResource
import java.nio.file.Path

object Home : Screen() {
    
    fun selectDirectory(path: Path) {
        val root = RootDirectory(path)
        if(!root.isValid()) return
        
        History.File.value.history.remove(path)
        History.File.value.history.addFirst(path)
        History.File.save()
        
        next { Editor(root) }
    }
    
    @Composable
    override fun Content() = Box(Modifier.fillMaxSize()) {
        
        Surface(Modifier.align(Alignment.Center).padding(10.dp).clip(Theme.Shapes.medium), color = Mocha.mantle) {
            Column(
                Modifier.fillMaxSize().padding(horizontal = 120.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(Res.drawable.pckd), null, Modifier.size(100.dp), tint = Color.Unspecified)
                    Spacer(Modifier.width(15.dp))
                    Text("Welcome to pckd", fontSize = 35.sp)
                }

                Text(
                    "Made by FlamingFire0",
                    fontSize = 12.sp,
                    color = Mocha.overlay1,
                )

                Spacer(Modifier.height(100.dp))

                val history = remember { ObservableList(History.File.value.history) }



                if (history.isNotEmpty()) {
                    Text("Recent Packs", fontSize = 25.sp)

                    Spacer(Modifier.height(10.dp))

                    Grouping {
                        history.forEach {

                            ContextMenu(
                                items = listOf(
                                    ContextAction("Open Directory") {
                                        it.openInFileManager()
                                    },
                                    ContextAction("Copy Path") {
                                        copyToClipboard(it.toString())
                                    },
                                    ContextAction("Remove") {
                                        history.remove(it)
                                        History.File.save()
                                    }
                                )

                            ) {
                                RowButton(onClick = { selectDirectory(it) }) {
                                    Text(
                                        it.minify(),
                                        fontSize = 18.sp,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(15.dp))
                }

                val directoryPicker = rememberDirectoryPickerLauncher(
                    onResult = { path ->
                        path?.path?.let {
                            selectDirectory(Path.of(it))
                        }
                    }
                )

                Button(onClick = { directoryPicker.launch() }) {
                    Text("Open Resource Pack")
                }
            }

            SmallButton(
                onClick = { 
                    Directories.config.path.openInFileManager() 
                },
                Modifier.align(Alignment.BottomEnd).padding(12.dp).size(50.dp).aspectRatio(1f),
                backgroundColor = Mocha.base,
            ) {
                Icon(Icons.Rounded.Settings, "Settings", Modifier.size(30.dp))
            }
        }
    }
}
