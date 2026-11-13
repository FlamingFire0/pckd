package me.flamingo.pckd.frontend.components.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.first
import me.flamingo.pckd.frontend.components.popup.Popup
import me.flamingo.pckd.frontend.components.popup.PopupStart
import me.flamingo.pckd.frontend.components.popup.PopupState
import me.flamingo.pckd.frontend.components.popup.PopupState.Companion.LocalPopupState
import me.flamingo.pckd.frontend.components.popup.noDragPopup
import me.flamingo.pckd.frontend.theming.Mocha
import me.flamingo.pckd.frontend.theming.Theme
import me.flamingo.pckd.frontend.utilities.ErrorHandler
import me.flamingo.pckd.frontend.utilities.platform.copyToClipboard

@Composable
fun ErrorDialog() {
    val error = ErrorHandler.error ?: return
    val state = remember(error) { PopupState(visible = true, start = PopupStart.Center) }

    LaunchedEffect(state) {
        snapshotFlow { state.visible }.first { !it }
        ErrorHandler.dismiss()
    }

    Popup(
        state = state,
        minWidth = 300.dp,
    ) {
        val state = requireNotNull(LocalPopupState.current)
        
        Column(
            Modifier.padding(10.dp).align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Something went wrong", Modifier.noDragPopup(), fontSize = 18.sp, color = Mocha.red)
            Spacer(Modifier.height(8.dp))
            Text(error::class.simpleName ?: "Error", Modifier.noDragPopup(), fontSize = 12.sp, color = Mocha.overlay2)
            Spacer(Modifier.height(4.dp))
            Text(error.message ?: "No message", Modifier.noDragPopup(), textAlign = TextAlign.Center)
            Spacer(Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.SpaceAround) {
                Button({ state.hide() }, Modifier.noDragPopup(), shape = Theme.Shapes.small) { Text("OK") }

                Spacer(Modifier.width(20.dp))

                Button(
                    { copyToClipboard(error.stackTraceToString()) },
                    Modifier.noDragPopup(),
                    shape = Theme.Shapes.small,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Mocha.surface0)
                ) { Text("Copy details") }

            }
        }
    }
}