package me.flamingo.pckd.frontend.utilities.platform


val toolkit: String = java.awt.Toolkit.getDefaultToolkit().javaClass.name
val isX11: Boolean = toolkit == "sun.awt.X11.XToolkit"