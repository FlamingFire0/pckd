package me.flamingo.pckd.frontend.components.contextmenu

data class ContextAction(
    val label: String,
    val enabled: Boolean = true,
    val onClick: () -> Unit,
)