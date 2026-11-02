package me.flamingo.pckd.frontend.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.vinceglb.filekit.dialogs.compose.rememberDirectoryPickerLauncher
import io.github.vinceglb.filekit.path
import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.config.History
import me.flamingo.pckd.frontend.Mocha
import me.flamingo.pckd.frontend.common.*
import me.flamingo.pckd.frontend.common.context_menu.ContextAction
import me.flamingo.pckd.frontend.common.context_menu.contextMenu
import me.flamingo.pckd.frontend.resources.Res
import me.flamingo.pckd.frontend.resources.pckd
import me.flamingo.pckd.frontend.screens.editor.Editor
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
    override fun content() {
        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(Res.drawable.pckd), null, Modifier.size(100.dp), tint = Color.Unspecified)
                    Spacer(Modifier.width(15.dp))
                    Text("Welcome to pckd", fontSize = 35.sp)
                }

                Spacer(Modifier.height(30.dp))

                val history = remember { ObservableList(History.File.value.history) }



                if (history.isNotEmpty()) {
                    Text("Recent Packs", fontSize = 25.sp)

                    Spacer(Modifier.height(10.dp))

                    grouping {
                        history.forEach {

                            contextMenu(
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
                                rowButton(onClick = { selectDirectory(it) }) {
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

            Text(
                "Made by FlamingFire0",
                fontSize = 12.sp,
                color = Mocha.overlay1,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
            )
        }
    }
}
