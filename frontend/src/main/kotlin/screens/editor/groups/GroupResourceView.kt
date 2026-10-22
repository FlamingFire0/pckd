package me.flamingo.pckd.frontend.screens.editor.groups

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import me.flamingo.pckd.backend.resources.Resource
import me.flamingo.pckd.frontend.common.grouping
import me.flamingo.pckd.frontend.common.rowButton
import kotlin.io.path.exists

@Composable
fun groupResourceView(
    name: String,
    resources: List<Resource<*, *>>
) {
    Column {
        Text(name)

        grouping {
            resources.forEach {
                if(!it.getFile().path.exists()) return@forEach

                rowButton(onClick = { it.open() }) {
                    Text(it.key.toString(true))
                }
            }
        }
    }
}