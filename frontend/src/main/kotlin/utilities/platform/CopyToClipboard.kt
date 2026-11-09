package me.flamingo.pckd.frontend.utilities.platform

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection


fun copyToClipboard(text: String) {
    Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(text), null)
}