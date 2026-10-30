package me.flamingo.pckd.frontend.screens.editor.groups

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import me.flamingo.pckd.backend.resources.ModelResource
import me.flamingo.pckd.backend.resources.Resource
import me.flamingo.pckd.frontend.common.context_menu.ContextAction
import me.flamingo.pckd.frontend.common.context_menu.contextMenu
import me.flamingo.pckd.frontend.common.grouping
import me.flamingo.pckd.frontend.common.openInFileManager
import me.flamingo.pckd.frontend.common.rowButton
import kotlin.io.path.exists

@Composable
fun groupResourceView(
    name: String,
    resources: List<Resource<*, *>>
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(name)

        grouping {
            resources.forEach {
                if(!it.getFile().path.exists()) return@forEach

                contextMenu(
                    listOfNotNull(
                        ContextAction("Open Parent Directory") {
                            it.getFile().path.parent.openInFileManager()
                        },
                        
                        if(it is ModelResource) ContextAction("Open as Text") { it.openText() } else null
                    )
                ) {
                    rowButton(onClick = { it.open() }) {
                        Text(it.key.toString(true))
                    }
                }
            }
        }
    }
}