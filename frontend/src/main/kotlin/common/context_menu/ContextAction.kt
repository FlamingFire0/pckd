package me.flamingo.pckd.frontend.common.context_menu

data class ContextAction(
    val label: String,
    val enabled: Boolean = true,
    val onClick: () -> Unit,
)