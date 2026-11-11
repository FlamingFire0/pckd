package me.flamingo.pckd.frontend.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.flamingo.pckd.frontend.components.popup.Popup
import me.flamingo.pckd.frontend.components.popup.PopupState
import me.flamingo.pckd.frontend.components.popup.noDragPopup
import me.flamingo.pckd.frontend.theming.Mocha
import me.flamingo.pckd.frontend.theming.Theme

@Composable
fun ConfirmationDialog(
    message: String,
    title: String? = null,
    otherActions: MutableMap<String, () -> Unit>.() -> Unit = {},
    onCancel:  () -> Unit = {},
    onConfirm: () -> Unit = {},
): PopupState {
    val confirm = "Confirm"
    val cancel = "Cancel"
    
    return Popup {
        val actions = mutableMapOf(
            confirm to onConfirm,
            cancel to onCancel,
        ).apply(otherActions)

        Column(
            Modifier
                .padding(10.dp)
                .width(IntrinsicSize.Max),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (title != null) {
                Text(title, Modifier.noDragPopup(), textAlign = TextAlign.Center)
                Spacer(Modifier.height(15.dp))
            }
            Text(message, Modifier.noDragPopup(), textAlign = TextAlign.Center)

            Spacer(Modifier.height(25.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {

                actions.forEach { action ->
                    val colors = if (action.key == confirm) ButtonDefaults.buttonColors()
                    else ButtonDefaults.buttonColors(
                        backgroundColor = Mocha.surface0
                    )

                    Button(
                        onClick = {
                            action.value()
                            it.hide()
                        },
                        modifier = Modifier.noDragPopup(),
                        colors = colors,
                        shape = Theme.Shapes.small,
                    ) {
                        Text(action.key)
                    }
                }
            }
        }
    }
}